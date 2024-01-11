package Caracteristics;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Classe pour la classification des frelons en donnant ca caste( ouvriere,fondatrice ou male)
 */
public class HornetClassification {


    /**
     * Fonction pour determiner si le frelon est male,ouvriere ou fondatrice.
     * @param pictureFile Chemin d'accès à l'image.
     * @return Caste du frelon.
     */
    public static Map<String, String> classifyHornet(Mat pictureFile) {
        // Ouverture du xml des paramètres
        Document parameters = parseXmlDocument("Results/parameters.xml");

        // Récupération des paramètres
        int seuil = Integer.parseInt(parameters.getElementsByTagName("seuil").item(0).getFirstChild().getNodeValue());
        int actualMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        int scale = Integer.parseInt(parameters.getElementsByTagName("echelle").item(0).getFirstChild().getNodeValue());
        int flag = Integer.parseInt(parameters.getElementsByTagName("forcer_debut_saison").item(0).getFirstChild().getNodeValue());
        System.out.println("mois actuel : " + actualMonth + " | seuil : " + seuil + " | échelle : " + scale + " | forcer début saison : " + flag);

        // Initialisation du dictionnaire de retour
        Map<String, String> characteristics = new HashMap<>();

        // Recherche de la longueur du frelon
        int[] hornetLengthValue = HornetLength.calculer_HornetLength(pictureFile);
        double reelLength = hornetLengthValue[0] / (double) scale;
        characteristics.put("Longeur du frelon:", String.valueOf(reelLength));

        // Recherche de la forme de l'abdomen
        Point stingCoordinates = new Point(hornetLengthValue[1], hornetLengthValue[2]);
        String abdomenShapeValue = AbdomenShape.abdomenShape(pictureFile, stingCoordinates);
        characteristics.put("Forme de l'abdomen du frelon:", abdomenShapeValue);

        // Détermination de la caste
        //if (actualMonth <= seuil && flag == 0) {
        // characteristics.put("cast", "Fondatrice");
        int year = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        String path = "Results/length" + year + ".xml";
        if (!new File(path).exists()) {
            Document doc = createXmlDocument();
            doc.getDocumentElement().normalize();
            Element data = doc.createElement("data");
            doc.appendChild(data);
            saveXmlDocument(doc, path);
        }
        Document doc = parseXmlDocument(path);
        Element data = (Element) doc.getElementsByTagName("data").item(0);
        Element length = doc.createElement("length");
        length.setAttribute("unit", "mm");
        length.appendChild(doc.createTextNode(String.valueOf(reelLength)));
        data.appendChild(length);
        saveXmlDocument(doc, path);
        // } else {

        NodeList lengthNodes = doc.getElementsByTagName("length");
        double minLength = 9999;
        for (int i = 0; i < lengthNodes.getLength(); i++) {
            double lengthValue = Double.parseDouble(lengthNodes.item(i).getFirstChild().getNodeValue());
            if (lengthValue < minLength) {
                minLength = lengthValue;
            }
        }
        double maxLength = Double.MIN_VALUE; // Initialize to the smallest possible double value
        for (int i = 0; i < lengthNodes.getLength(); i++) {
            double lengthValue = Double.parseDouble(lengthNodes.item(i).getFirstChild().getNodeValue());
            if (lengthValue > maxLength) {
                maxLength = lengthValue;
            }
        }
        System.out.println("Longeur Minimale : " + minLength);
        System.out.println("Longeur Maximale : " + maxLength);
        //Determination de la caste du frelon en se basant sur la longeur minimale
        if (abdomenShapeValue != null) {
            if (abdomenShapeValue.equals("pointu") && reelLength >= maxLength) {
                characteristics.put("cast", "Reine ou Fondatrice");

            } else if (abdomenShapeValue.equals("pointu") && (reelLength >= minLength || reelLength < maxLength)) {
                characteristics.put("cast", "Ouvriere");

            } else if (abdomenShapeValue.equals("rond")) {
                characteristics.put("cast", "Male");

            }
        }

        return characteristics;
    }

    /**
     * Cette méthode parse un document XML à partir d'un fichier spécifié.
     *
     * @param filePath Le chemin du fichier XML à parser.
     * @return Un objet Document représentant la structure du fichier XML, ou null en cas d'erreur.
     */
    private static Document parseXmlDocument(String filePath) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            // Parse le fichier XML et retourne le document résultant
            return builder.parse(new File(filePath));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Cette méthode crée un nouveau document XML vide.
     *
     * @return Un nouvel objet Document représentant un document XML vide, ou null en cas d'erreur.
     */
    private static Document createXmlDocument() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            // Crée et retourne un nouveau document XML vide
            return builder.newDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Cette méthode enregistre un document XML dans un fichier spécifié.
     *
     * @param doc      Le document XML à enregistrer.
     * @param filePath Le chemin du fichier dans lequel enregistrer le document XML.
     */
    private static void saveXmlDocument(Document doc, String filePath) {
        try {
            // Crée un transformateur pour formater la sortie XML
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
            // Enregistre le document XML dans le fichier spécifié
            transformer.transform(new DOMSource(doc), new StreamResult(new File(filePath)));
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

}



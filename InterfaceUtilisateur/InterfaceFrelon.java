package InterfaceUtilisateur;

import javax.swing.*;
import Caracteristics.HornetClassification;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import CutOut.CutOut;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Map;

/**
 * Interface graphique pour le traitement d'images de frelon.
 */
public class InterfaceFrelon extends JFrame {

    // Composants de l'interface utilisateur
    private JTextField numberField;
    private JButton processButton;
    private JLabel resultLabel;

    // Chemin de l'image à traiter
    private String imagePath;

    /**
     * Constructeur de la classe InterfaceFrelon.
     * Initialise la fenêtre et les composants.
     */
    public InterfaceFrelon() {
        // Configuration de la fenêtre
        setTitle("Application pour traitement d'images de frelon");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialisation des composants
        numberField = new JTextField();
        processButton = new JButton("Appuyer pour traitement");
        resultLabel = new JLabel("Results:");

        // Ajout des composants à la fenêtre
        add(numberField, BorderLayout.NORTH);
        add(processButton, BorderLayout.CENTER);
        add(resultLabel, BorderLayout.SOUTH);

        // Ajout de l'écouteur de bouton
        processButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processImage();
            }
        });
    }

    /**
     * Méthode pour traiter l'image en fonction du nombre saisi par l'utilisateur.
     */
    private void processImage() {
        try {
            // Récupérer le nombre saisi par l'utilisateur
            int userNumber = Integer.parseInt(numberField.getText());

            // Construire le chemin d'accès à l'image
            imagePath = "Footage/" + userNumber + ".jpg";

            // Vérifier si le fichier d'image existe
            File imageFile = new File(imagePath);
            if (!imageFile.exists()) {
                resultLabel.setText("Error: L'image n'existe pas dans le dossier Footage.");
                return;
            }

            // Vérifier si le cutout existe
            String cutoutPath = "Footage/" + userNumber + "_cutout.jpg";
            File cutoutFile = new File(cutoutPath);

            if (!cutoutFile.exists()) {
                // Le cutout n'existe pas, le créer en utilisant la classe CutOut
                CutOut.cutOut(imagePath);
            }

            // Appel de la méthode classifyHornet de HornetClassification
            Map<String, String> characteristics = HornetClassification.classifyHornet(cutoutPath);

            // Afficher les résultats dans l'interface Swing
            resultLabel.setText("<html>Results:<br>");
            for (Map.Entry<String, String> entry : characteristics.entrySet()) {
                resultLabel.setText(resultLabel.getText() + entry.getKey() + " " + entry.getValue() + "<br>");
            }
            resultLabel.setText(resultLabel.getText() + "</html>");
        } catch (NumberFormatException ex) {
            // Gérer l'erreur si l'utilisateur entre un nombre invalide
            resultLabel.setText("Error: Entrez un nombre valide.");
        } catch (Exception ex) {
            // Gérer d'autres erreurs possibles
            resultLabel.setText("Error: Pas de traitment possible sur cette image");
        }
    }

    /**
     * Méthode principale pour lancer l'interface utilisateur.
     * Charge la bibliothèque native d'OpenCV et crée une instance de InterfaceFrelon.
     */
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        SwingUtilities.invokeLater(() -> {
            InterfaceFrelon gui = new InterfaceFrelon();
            gui.setVisible(true);
        });
    }
}

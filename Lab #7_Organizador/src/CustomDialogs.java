
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class CustomDialogs {

    public static String mostrarInputDialog(String mensaje) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        JLabel label = new JLabel(mensaje);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setForeground(Color.BLACK);

        JTextField campoTexto = new JTextField(15);
        campoTexto.setFont(new Font("Arial", Font.PLAIN, 14));
        campoTexto.setBorder(new LineBorder(new Color(52, 152, 219), 2));

        panel.add(label, BorderLayout.NORTH);
        panel.add(campoTexto, BorderLayout.CENTER);

        UIManager.put("OptionPane.okButtonText", "Aceptar");
        UIManager.put("OptionPane.cancelButtonText", "Cancelar");

        int resultado = JOptionPane.showConfirmDialog(null, panel, "Input", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null);

        if (resultado == JOptionPane.OK_OPTION) {
            return campoTexto.getText();
        } else {
            return null;
        }
    }

    public static void mostrarMensaje(String mensaje, String titulo, int tipoMensaje) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        JLabel label = new JLabel(mensaje);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setForeground(Color.BLACK);

        panel.add(label, BorderLayout.CENTER);

        UIManager.put("OptionPane.okButtonText", "Aceptar");

        JOptionPane.showMessageDialog(null, panel, titulo, tipoMensaje, null);
    }

    public static void mostrarMensajeError(String mensaje) {
        mostrarMensaje(mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void mostrarMensajeExito(String mensaje) {
        mostrarMensaje(mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
}

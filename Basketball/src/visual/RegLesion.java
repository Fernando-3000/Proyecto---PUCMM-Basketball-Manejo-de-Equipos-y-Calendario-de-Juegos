package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import logico.Jugador;
import logico.Lesion;
import logico.SerieNacional;
import SQL.DatabaseManager;

public class RegLesion extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();

    private JTextField txtId;
    private JTextField txtFechaRec;
    private JTextField txtIdJugador;
    private JTextField txtNomJugador;
    private JComboBox<String> cmbxTipoLesion;
    private JSpinner spnDiasReposo;
    private LocalDate today;
    private LocalDate recuperacion;
    private JButton okButton;
    private JButton cancelButton;
    private JTextArea txtDescripcion;
    private JRadioButton rdLesion;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private Jugador jugador;
    private Lesion lesionEdit = null;

    public RegLesion(Jugador jug, Lesion aux) {
        super(); // ✅ super() es lo primero

        this.jugador = jug;
        this.lesionEdit = aux;

        setResizable(false);
        setModal(true);
        setTitle(aux == null ? "Registrar Lesión" : "Modificar Lesión");
        setBounds(100, 100, 513, 380);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());

        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        today = LocalDate.now();
        recuperacion = today;

        // === ID ===
        JLabel lblId = new JLabel("ID:");
        lblId.setBounds(91, 17, 20, 16);
        contentPanel.add(lblId);

        txtId = new JTextField();
        txtId.setEditable(false);
        txtId.setColumns(10);
        if (aux == null) {
            txtId.setText("LE-" + SerieNacional.getInstance().getGeneradorLesion());
        } else {
            txtId.setText(aux.getId());
        }
        txtId.setBounds(113, 10, 226, 25);
        contentPanel.add(txtId);

        // === Estado ===
        JLabel lblEstado = new JLabel("Estado:");
        lblEstado.setBounds(351, 13, 52, 16);
        contentPanel.add(lblEstado);

        rdLesion = new JRadioButton("Activa");
        rdLesion.setSelected(true);
        rdLesion.setEnabled(aux == null); // Solo se puede cambiar al registrar
        rdLesion.setBounds(396, 9, 90, 25);
        rdLesion.addActionListener(e -> {
            rdLesion.setText(rdLesion.isSelected() ? "Activa" : "No Activa");
        });
        contentPanel.add(rdLesion);

        // === Jugador ===
        JLabel lblJugador = new JLabel("Jugador:");
        lblJugador.setBounds(56, 45, 52, 16);
        contentPanel.add(lblJugador);

        txtIdJugador = new JTextField(jug.getId());
        txtIdJugador.setEditable(false);
        txtIdJugador.setBounds(113, 42, 122, 25);
        contentPanel.add(txtIdJugador);

        txtNomJugador = new JTextField(jug.getNombre() + " " + jug.getApellido());
        txtNomJugador.setEditable(false);
        txtNomJugador.setBounds(239, 42, 247, 25);
        contentPanel.add(txtNomJugador);

        // === Tipo de Lesión ===
        JLabel lblTipo = new JLabel("Tipo de lesión:");
        lblTipo.setBounds(23, 80, 90, 16);
        contentPanel.add(lblTipo);

        cmbxTipoLesion = new JComboBox<>();
        cmbxTipoLesion.setModel(new DefaultComboBoxModel<>(new String[]{
            "Seleccionar", "Esguince", "Fractura", "Lesión ligamentosa",
            "Desgarro muscular", "Contusión", "Luxación", "Distensión",
            "Tensión muscular", "Lesión ósea", "Lesión articular",
            "Lesión tendinosa", "Otra"
        }));
        cmbxTipoLesion.setBounds(113, 78, 373, 25);
        contentPanel.add(cmbxTipoLesion);

        // === Descripción ===
        JLabel lblDesc = new JLabel("Descripción:");
        lblDesc.setBounds(35, 115, 74, 16);
        contentPanel.add(lblDesc);

        txtDescripcion = new JTextArea();
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setFont(new Font("Tahoma", Font.PLAIN, 11));
        JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
        scrollDesc.setBounds(113, 110, 373, 60);
        contentPanel.add(scrollDesc);

        // === Días de reposo ===
        JLabel lblDias = new JLabel("Días de reposo:");
        lblDias.setBounds(17, 185, 90, 16);
        contentPanel.add(lblDias);

        spnDiasReposo = new JSpinner(new SpinnerNumberModel(0, 0, 365, 1));
        spnDiasReposo.setBounds(113, 182, 52, 25);
        spnDiasReposo.addChangeListener(e -> {
            int dias = (int) spnDiasReposo.getValue();
            recuperacion = today.plusDays(dias);
            txtFechaRec.setText(recuperacion.format(formatter));
        });
        contentPanel.add(spnDiasReposo);

        // === Fecha de recuperación ===
        JLabel lblFechaRec = new JLabel("Fecha de recuperación:");
        lblFechaRec.setBounds(254, 185, 143, 16);
        contentPanel.add(lblFechaRec);

        txtFechaRec = new JTextField();
        txtFechaRec.setHorizontalAlignment(SwingConstants.CENTER);
        txtFechaRec.setEditable(false);
        txtFechaRec.setText(recuperacion.format(formatter));
        txtFechaRec.setBounds(396, 182, 90, 25);
        contentPanel.add(txtFechaRec);

        // === Botones ===
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        okButton = new JButton(aux == null ? "Registrar" : "Modificar");
        okButton.setFont(new Font("Tahoma", Font.BOLD, 13));
        okButton.addActionListener(e -> {
            if (validarDatos()) {
                guardarLesion();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Por favor, complete todos los campos correctamente.",
                    "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            }
        });
        buttonPane.add(okButton);
        getRootPane().setDefaultButton(okButton);

        cancelButton = new JButton("Cancelar");
        cancelButton.setFont(new Font("Tahoma", Font.BOLD, 13));
        cancelButton.addActionListener(e -> dispose());
        buttonPane.add(cancelButton);

        // === Cargar datos si es modificación ===
        if (aux != null) {
            cargarDatosEdicion(aux);
        }
    }

    private void cargarDatosEdicion(Lesion aux) {
        cmbxTipoLesion.setSelectedItem(aux.getTipoDeLesion());
        spnDiasReposo.setValue((int) ChronoUnit.DAYS.between(aux.getFechaLes(), aux.getFechaRecPrevista()));
        txtDescripcion.setText(aux.getDescripcionCorta());
        rdLesion.setSelected(aux.isEstado());
        rdLesion.setText(aux.isEstado() ? "Activa" : "No Activa");
        today = aux.getFechaLes();
        recuperacion = aux.getFechaRecPrevista();
    }

    private boolean validarDatos() {
        String tipo = (String) cmbxTipoLesion.getSelectedItem();
        String desc = txtDescripcion.getText().trim();
        int dias = (int) spnDiasReposo.getValue();

        return tipo != null && !tipo.equals("Seleccionar") &&
               dias > 0 &&
               desc.length() >= 5;
    }

    private void guardarLesion() {
        String tipoLesion = (String) cmbxTipoLesion.getSelectedItem();
        String descripcion = txtDescripcion.getText().trim();

        if (lesionEdit == null) {
            // Nueva lesión
            Lesion nuevaLesion = new Lesion(
                txtId.getText(),
                jugador,
                today,
                tipoLesion,
                recuperacion,
                descripcion,
                rdLesion.isSelected()
            );

            // Guardar en memoria
            jugador.getMisLesiones().add(nuevaLesion);
            SerieNacional.getInstance().modificarJugador(jugador);

            // ✅ Guardar en base de datos
            if (DatabaseManager.registrarLesion(
                nuevaLesion.getId(),
                jugador.getId(),
                today,
                tipoLesion,
                recuperacion,
                descripcion,
                rdLesion.isSelected()
            )) {
                JOptionPane.showMessageDialog(this, "Lesión registrada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else {
            // Modificar lesión existente
            lesionEdit.setTipoDeLesion(tipoLesion);
            lesionEdit.setFechaRecPrevista(recuperacion);
            lesionEdit.setDescripcionCorta(descripcion);
            lesionEdit.setEstado(rdLesion.isSelected());

            // ✅ Actualizar en base de datos
            if (DatabaseManager.actualizarLesion(
                lesionEdit.getId(),
                tipoLesion,
                recuperacion,
                descripcion,
                rdLesion.isSelected()
            )) {
                SerieNacional.getInstance().modificarLesion(lesionEdit);
                JOptionPane.showMessageDialog(this, "Lesión modificada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        // Actualizar listado
        for (Window window : Window.getWindows()) {
            if (window instanceof ListadoLesiones) {
                ((ListadoLesiones) window).loadAll();
            }
        }
    }
}
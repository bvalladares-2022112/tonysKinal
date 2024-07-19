package org.brenervalladares.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.brenervalladares.bean.TipoEmpleado;
import org.brenervalladares.db.Conexion;
import org.brenervalladares.main.Principal;

public class TipoEmpleadoController  implements Initializable {
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<TipoEmpleado> listaTipoEmpleado;

    @FXML private TextField txtCodigoTipoEmpleado;
    @FXML private TextField txtDescripcion;
    @FXML private TableView tblTipoEmpleado;
    @FXML private TableColumn colCodigoTipoEmpleado;
    @FXML private TableColumn colDescripcion;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    
    
        @Override
        public void initialize(URL location, ResourceBundle resources) {
            cargarDatos();
    
        }
        
        public void cargarDatos(){
            tblTipoEmpleado.setItems(getTipoEmpleado());
            colCodigoTipoEmpleado.setCellValueFactory(new PropertyValueFactory<TipoEmpleado, Integer>("codigoTipoEmpleado"));
            colDescripcion.setCellValueFactory(new PropertyValueFactory<TipoEmpleado, String> ("descripcion"));
        }
        
        public ObservableList<TipoEmpleado> getTipoEmpleado(){
            ArrayList<TipoEmpleado> lista = new ArrayList<TipoEmpleado>();
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListartipoEmpleado");
                ResultSet resultado = procedimiento.executeQuery();
                while(resultado.next()){
                    lista.add(new TipoEmpleado (resultado.getInt("codigoTipoEmpleado"),       
                                resultado.getString("descripcion")));
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            return listaTipoEmpleado = FXCollections.observableArrayList(lista); 
        }
        
        public void nuevo(){
            switch(tipoDeOperacion){
                case NINGUNO:
                    activarControles();
                    btnNuevo.setText("Guardar");
                    btnEliminar.setText("Cancelar");
                    btnEditar.setDisable(true);
                    btnReporte.setDisable(true);
                    imgNuevo.setImage(new Image("org/brenervalladares/image/guardar.png"));
                    imgEliminar.setImage(new Image("org/brenervalladares/image/cancelar.png"));
                    tipoDeOperacion = operaciones.GUARDAR;
                    break;    
                case GUARDAR:
                    guardar();
                    limpiarControles();
                    desactivarControles();
                    btnNuevo.setText("Nuevo");
                    btnEliminar.setText("Eliminar");
                    btnEditar.setDisable(false);
                    btnReporte.setDisable(false);
                    imgNuevo.setImage(new Image("org/brenervalladares/image/agregar.png"));
                    imgEliminar.setImage(new Image("org/brenervalladares/image/eliminar.png"));
                    tipoDeOperacion = operaciones.NINGUNO;
                    cargarDatos();
                    break;
            }
        }
        
        public void seleccionarElemento(){
            if (tblTipoEmpleado.getSelectionModel().getSelectedItem() != null){
            txtCodigoTipoEmpleado.setText(String.valueOf(((TipoEmpleado)tblTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado()));
            txtDescripcion.setText(((TipoEmpleado)tblTipoEmpleado.getSelectionModel().getSelectedItem()).getDescripcion());
        }else {
            JOptionPane.showMessageDialog(null, "Debe Seleccionar Un Campo");
            }
     }
        
        public void eliminar(){
            switch(tipoDeOperacion){
                case GUARDAR:
                    limpiarControles();
                    desactivarControles();
                    btnNuevo.setText("Nuevo");
                    btnEliminar.setText("Eliminar");
                    btnEditar.setDisable(false);
                    btnReporte.setDisable(false);
                    imgNuevo.setImage(new Image("org/brenervalladares/image/agregar.png"));
                    imgEliminar.setImage(new Image("org/brenervalladares/image/eliminar.png"));
                    tipoDeOperacion = operaciones.NINGUNO;
                    break;
                default:
                    if (tblTipoEmpleado.getSelectionModel().getSelectedItem() != null) {
                        int respuesta = JOptionPane.showConfirmDialog(null, "¿Estas seguro de eliminar el registro?", "Eliminar TipoEmpleado", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (respuesta == JOptionPane.YES_OPTION) {
                            try {
                                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarTipoEmpleado(?)");
                                procedimiento.setInt(1, ((TipoEmpleado) tblTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
                                listaTipoEmpleado.remove(tblTipoEmpleado.getSelectionModel().getSelectedIndex());
                                limpiarControles();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else if(respuesta == JOptionPane.NO_OPTION){
                            limpiarControles();
                            desactivarControles();
                            tblTipoEmpleado.getSelectionModel().clearSelection();
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                    }
                
            }
        
        }
        
        
    public void guardar(){
        TipoEmpleado registro = new TipoEmpleado();
        //registro.setCodigoTipoEmpleado(Integer.parseInt(txtCodigoTipoEmpleado.getText()));
          if(txtDescripcion.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Falta un dato por llenar");
        } else {
        registro.setDescripcion(txtDescripcion.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarTipoEmpleado(?)");
            procedimiento.setString(1, registro.getDescripcion());
            procedimiento.execute();
            listaTipoEmpleado.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }    
}    
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
            if  (tblTipoEmpleado.getSelectionModel().getSelectedItem() != null){
                btnNuevo.setDisable(true);
                btnEliminar.setDisable(true);
                btnEditar.setText("Actualizar");
                btnReporte.setText("Cancelar");
                imgEditar.setImage(new Image("org/brenervalladares/image/actualizar.png"));
                imgReporte.setImage(new Image("org/brenervalladares/image/cancelar.png") );
                activarControles();
                tipoDeOperacion = operaciones.ACTUALIZAR;
            }else {
                JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
            }
            break;
            case ACTUALIZAR:
                actualizar();
                limpiarControles();
                desactivarControles();
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                imgEditar.setImage(new Image("org/brenervalladares/image/editar.png"));
                imgReporte.setImage(new Image("org/brenervalladares/image/reporte.png"));
                cargarDatos();
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }
    
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarTipoEmpleado(?,?)");
            TipoEmpleado registro = (TipoEmpleado)tblTipoEmpleado.getSelectionModel().getSelectedItem();
            registro.setDescripcion(txtDescripcion.getText());
            procedimiento.setInt(1, registro.getCodigoTipoEmpleado());
            procedimiento.setString(2, registro.getDescripcion());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
        
     public void reporte() {
        switch (tipoDeOperacion) {
            case ACTUALIZAR:
                limpiarControles();
                desactivarControles();
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setText("Editar"); 
                btnReporte.setText("Reporte");
                imgEditar.setImage(new Image("/org/brenervalladares/image/editar.png"));
                imgReporte.setImage(new Image("/org/brenervalladares/image/reporte.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                tblTipoEmpleado.getSelectionModel().clearSelection();
        }
    }
    
    public void desactivarControles(){
        txtCodigoTipoEmpleado.setEditable(false);
        txtDescripcion.setEditable(false);
    }
     public void activarControles(){
         txtCodigoTipoEmpleado.setEditable(false);
         txtDescripcion.setEditable(true);
     }
     
     public void limpiarControles(){
         txtCodigoTipoEmpleado.clear();
         txtDescripcion.clear();
     }
     
     
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
}



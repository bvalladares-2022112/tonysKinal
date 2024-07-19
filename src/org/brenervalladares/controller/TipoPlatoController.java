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
import org.brenervalladares.bean.TipoPlato;
import org.brenervalladares.db.Conexion;
import org.brenervalladares.main.Principal;

public class TipoPlatoController  implements Initializable {
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<TipoPlato> listaTipoPlato;

    @FXML private TextField txtCodigoTipoPlato;
    @FXML private TextField txtDescripcionTipo;
    @FXML private TableView tblTipoPlato;
    @FXML private TableColumn colCodigoTipoPlato;
    @FXML private TableColumn colDescripcionTipo;
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
            tblTipoPlato.setItems(getTipoPlato());
            colCodigoTipoPlato.setCellValueFactory(new PropertyValueFactory<TipoPlato, Integer>("codigoTipoPlato"));
            colDescripcionTipo.setCellValueFactory(new PropertyValueFactory<TipoPlato, String> ("descripcionTipo"));
        }
        
        public ObservableList<TipoPlato> getTipoPlato(){
            ArrayList<TipoPlato> lista = new ArrayList<TipoPlato>();
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarTipoPlato");
                ResultSet resultado = procedimiento.executeQuery();
                while(resultado.next()){
                    lista.add(new TipoPlato (resultado.getInt("codigoTipoPlato"),       
                                resultado.getString("descripcionTipo")));
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            return listaTipoPlato= FXCollections.observableArrayList(lista); 
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
            if  (tblTipoPlato.getSelectionModel().getSelectedItem() != null){
            txtCodigoTipoPlato.setText(String.valueOf(((TipoPlato)tblTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato()));
            txtDescripcionTipo.setText(((TipoPlato)tblTipoPlato.getSelectionModel().getSelectedItem()).getDescripcionTipo());
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
                    if (tblTipoPlato.getSelectionModel().getSelectedItem() != null) {
                        int respuesta = JOptionPane.showConfirmDialog(null, "¿Estas seguro de eliminar el registro?", "Eliminar TipoPlato", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (respuesta == JOptionPane.YES_OPTION) {
                            try {
                                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarTipoPlato(?)");
                                procedimiento.setInt(1, ((TipoPlato) tblTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
                                listaTipoPlato.remove(tblTipoPlato.getSelectionModel().getSelectedIndex());
                                limpiarControles();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else if(respuesta == JOptionPane.NO_OPTION){
                            limpiarControles();
                            desactivarControles();
                            tblTipoPlato.getSelectionModel().clearSelection();
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                    }
                
            }
        
        }
        
        
    public void guardar(){
        TipoPlato registro = new TipoPlato();
        //registro.setCodigoTipoPlato(Integer.parseInt(txtCodigoTipoPlato.getText()));
        registro.setDescripcionTipo(txtDescripcionTipo.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarTipoPlato(?)");
            procedimiento.setString(1, registro.getDescripcionTipo());
            procedimiento.execute();
            listaTipoPlato.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }    
        
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
            if  (tblTipoPlato.getSelectionModel().getSelectedItem() != null){
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
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarTipoPlato(?,?)");
            TipoPlato registro = (TipoPlato)tblTipoPlato.getSelectionModel().getSelectedItem();
            registro.setDescripcionTipo(txtDescripcionTipo.getText());
            procedimiento.setInt(1, registro.getCodigoTipoPlato());
            procedimiento.setString(2, registro.getDescripcionTipo());
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
                tblTipoPlato.getSelectionModel().clearSelection();
        }
    }
    
    public void desactivarControles(){
        txtCodigoTipoPlato.setEditable(false);
        txtDescripcionTipo.setEditable(false);
    }
     public void activarControles(){
         txtCodigoTipoPlato.setEditable(false);
         txtDescripcionTipo.setEditable(true);
     }
     
     public void limpiarControles(){
         txtCodigoTipoPlato.clear();
         txtDescripcionTipo.clear();
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



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
import org.brenervalladares.bean.Producto;
import org.brenervalladares.db.Conexion;
import org.brenervalladares.main.Principal;


public class ProductoController  implements Initializable {
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<Producto> listaProducto;

    @FXML private TextField txtCodigoProducto;
    @FXML private TextField txtNombreProducto;
    @FXML private TextField txtCantidad;
    @FXML private TableView tblProductos;
    @FXML private TableColumn colCodigoProducto;
    @FXML private TableColumn colNombreProducto;
    @FXML private TableColumn colCantidad;
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
            tblProductos.setItems(getProducto());
            colCodigoProducto.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("codigoProducto"));
            colNombreProducto.setCellValueFactory(new PropertyValueFactory<Producto, String> ("nombreProducto"));
            colCantidad.setCellValueFactory(new PropertyValueFactory<Producto, String>("cantidad"));
        }
        
        public ObservableList<Producto> getProducto(){
            ArrayList<Producto> lista = new ArrayList<Producto>();
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarProducto");
                ResultSet resultado = procedimiento.executeQuery();
                while(resultado.next()){
                    lista.add(new Producto (resultado.getInt("codigoProducto"),       
                                resultado.getString("nombreProducto"),
                                resultado.getInt("cantidad")));
 
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            return listaProducto = FXCollections.observableArrayList(lista); 
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
                    tipoDeOperacion = ProductoController.operaciones.GUARDAR;
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
                    tipoDeOperacion = ProductoController.operaciones.NINGUNO;
                    cargarDatos();
                    break;
            }
        }
        
        public void seleccionarElemento(){
            if (tblProductos.getSelectionModel().getSelectedItem() != null){
            txtCodigoProducto.setText(String.valueOf(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto()));
            txtNombreProducto.setText(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getNombreProducto());
            txtCantidad.setText(String.valueOf(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCantidad()));
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
                    tipoDeOperacion = ProductoController.operaciones.NINGUNO;
                    break;
                default:
                    if (tblProductos.getSelectionModel().getSelectedItem() != null) {
                        int respuesta = JOptionPane.showConfirmDialog(null, "¿Estas seguro de eliminar el registro?", "Eliminar Producto", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (respuesta == JOptionPane.YES_OPTION) {
                            try {
                                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarProducto(?)");
                                procedimiento.setInt(1, ((Producto) tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto());
                                procedimiento.execute();
                                listaProducto.remove(tblProductos.getSelectionModel().getSelectedIndex());
                                limpiarControles();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else if(respuesta == JOptionPane.NO_OPTION){
                            limpiarControles();
                            desactivarControles();
                            tblProductos.getSelectionModel().clearSelection();
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                    }
                
            }
        
        }
        
        
    public void guardar(){
        Producto registro = new Producto();
        //registro.setCodigoProducto(Integer.parseInt(txtCodigoProducto.getText()));
          if(txtNombreProducto.getText().isEmpty() || txtCantidad.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Falta un dato por llenar");
        } else {
        registro.setNombreProducto(txtNombreProducto.getText());
        registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarProducto(?,?)");
            procedimiento.setString(1, registro.getNombreProducto());
            procedimiento.setInt(2, registro.getCantidad());
            procedimiento.execute();
            listaProducto.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }  
}
        
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
            if  (tblProductos.getSelectionModel().getSelectedItem() != null){
                btnNuevo.setDisable(true);
                btnEliminar.setDisable(true);
                btnEditar.setText("Actualizar");
                btnReporte.setText("Cancelar");
                imgEditar.setImage(new Image("org/brenervalladares/image/actualizar.png"));
                imgReporte.setImage(new Image("org/brenervalladares/image/cancelar.png") );
                activarControles();
                tipoDeOperacion = ProductoController.operaciones.ACTUALIZAR;
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
                tipoDeOperacion = ProductoController.operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarProducto(?,?,?)");
            Producto registro = (Producto)tblProductos.getSelectionModel().getSelectedItem();
            registro.setNombreProducto(txtNombreProducto.getText());
            registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
            procedimiento.setInt(1, registro.getCodigoProducto());
            procedimiento.setString(2, registro.getNombreProducto());
            procedimiento.setInt(3, registro.getCantidad());
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
                tblProductos.getSelectionModel().clearSelection();
        }
    }  
        

   public void desactivarControles(){
        txtCodigoProducto.setEditable(false);
        txtNombreProducto.setEditable(false);
        txtCantidad.setEditable(false);
    }
     public void activarControles(){
         txtCodigoProducto.setEditable(false);
         txtNombreProducto.setEditable(true);
         txtCantidad.setEditable(true);
     }
     
     public void limpiarControles(){
         txtCodigoProducto.clear();
         txtNombreProducto.clear();
         txtCantidad.clear();
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
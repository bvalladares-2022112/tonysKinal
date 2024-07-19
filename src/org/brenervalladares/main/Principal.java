/*Documentación
    Brener Roberto Valladares Chián
    Carné: 2022112
    Codigo Técnico: IN5AM
    Fecha de creación: 24/03/2023
    Fecha de modificación:
 */
package org.brenervalladares.main;

import java.io.IOException;
import java.io.InputStream;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.brenervalladares.controller.EmpresaController;
import org.brenervalladares.controller.LoginController;
import org.brenervalladares.controller.MenuPrincipalController;
import org.brenervalladares.controller.PresupuestoController;
import org.brenervalladares.controller.ProductoController;
import org.brenervalladares.controller.ProgramadorController;
import org.brenervalladares.controller.ServicioController;
import org.brenervalladares.controller.TipoEmpleadoController;
import org.brenervalladares.controller.TipoPlatoController;
import org.brenervalladares.controller.UsuarioController;


public class Principal extends Application {
    private final String PAQUETE_VISTA = "/org/brenervalladares/view/";
    private  Stage escenarioPrincipal;
    private Scene escena;
    
    @Override
    public void start(Stage escenarioPrincipal) throws Exception {
        this.escenarioPrincipal = escenarioPrincipal;
        this.escenarioPrincipal.setTitle("Tony's Kinal 2023");
        escenarioPrincipal.getIcons().add(new Image("/org/brenervalladares/image/Menu.jpg"));
        //Parent root = FXMLLoader.load(getClass().getResource("/org/brenervalladares/view/MenuPrincipalView.fxml"));
        //Scene escena = new Scene(root);
        //escenarioPrincipal.setScene(escena);
        login();
        escenarioPrincipal.show();
    }

    
    public void menuPrincipal(){
        try{
            MenuPrincipalController menu = (MenuPrincipalController)cambiarEscena("MenuPrincipalView.fxml",545,538);
            menu.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaProgramador(){
        try{
            ProgramadorController programador = (ProgramadorController)cambiarEscena("ProgramadorView.fxml",811,506);
            programador.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaEmpresa(){
        try{
         EmpresaController empresa = (EmpresaController)cambiarEscena("EmpresaView.fxml", 906, 472);
         empresa.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaProducto(){
        try{
         ProductoController producto = (ProductoController)cambiarEscena("ProductoView.fxml", 906, 472);
         producto.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaTipoPlato(){
        try{
         TipoPlatoController tipoPlato = (TipoPlatoController)cambiarEscena("TipoPlatoView.fxml", 906, 472);
         tipoPlato.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaTipoEmpleado(){
        try{
         TipoEmpleadoController tipoEmpleado = (TipoEmpleadoController)cambiarEscena("TipoEmpleadoView.fxml", 906, 472);
         tipoEmpleado.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaPresupuesto(){
        try{
            PresupuestoController presupuesto = (PresupuestoController)cambiarEscena("PresupuestoView.fxml", 906, 472);
            presupuesto.setEscenarioPrincipal(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void login(){
        try{
            LoginController login = (LoginController)cambiarEscena("LoginView.fxml",700,500);
            login.setEscenarioPrincipal(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaUsuario(){
    try{
        UsuarioController usuarioController = (UsuarioController)cambiarEscena("UsuarioView.fxml",906,472);
        usuarioController.setEscenarioPrincipal(this);
    }catch(Exception e){
        e.printStackTrace();
    }
  }
    
    public void ventanaServicio(){
    try{
        ServicioController servicioController = (ServicioController)cambiarEscena("ServicioView.fxml",1201,472);
        servicioController.setEscenarioPrincipal(this);
    }catch(Exception e){
        e.printStackTrace();
    }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
    public Initializable cambiarEscena(String fxml, int ancho, int alto) throws IOException{
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA+fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA+fxml));
        escena = new Scene((AnchorPane)cargadorFXML.load(archivo),ancho,alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable)cargadorFXML.getController();
        return resultado;
    }
    
}

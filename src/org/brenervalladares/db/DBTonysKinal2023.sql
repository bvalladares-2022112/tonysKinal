/*
	Nombre: Brener Roberto Valladares Chián
    Carné: 2022112
    Codigo Técnico: IN5AM
    Fecha Creación: 24/03/2023
    Fechas de modificacion: 
*/

drop database if exists DBTonysKinal2023;
Create database DBTonysKinal2023;
use DBTonysKinal2023;

Create table Empresas(
	codigoEmpresa int not null auto_increment,
    nombreEmpresa varchar(150) not null,
    direccion varchar(150) not null,
    telefono varchar(8) not null,
    primary key PK_codigoEmpresa(codigoEmpresa)
);

Create table TipoEmpleado(
	codigoTipoEmpleado int not null auto_increment,
    descripcion varchar(150) not null,
    primary key PK_codigoTipoEmpleado(codigoTipoEmpleado)
);

Create table TipoPlato(
	codigoTipoPlato int not null auto_increment,
    descripcionTipo varchar(150) not null,
    primary key PK_codigoTipoPlato(codigoTipoPlato)
);

Create table Producto(
	codigoProducto int not null auto_increment,
    nombreProducto varchar(150) not null,
    cantidad int not null,
    primary key PK_codigoProducto(codigoProducto)
);

create table Empleados(
	codigoEmpleado int not null auto_increment,
    numeroEmpleado int not null,
    apellidoEmpleado varchar(150) not null,
    nombresEmpleado varchar(150) not null,
    direccionEmpleado varchar(150) not null,
    telefonoContacto varchar(8) not null,
    gradoCocinero varchar(50),
    codigoTipoEmpleado int not null,
    primary key PK_codigoEmpleado(codigoEmpleado),
    constraint FK_Empleados_TipoEmpleado foreign key
		 (codigoTipoEmpleado)references TipoEmpleado(codigoTipoEmpleado)
);


create table Servicios(
	codigoServicio int not null auto_increment,
    fechaServicio date not null,
    tipoServicio varchar(150) not null,
    horaServicio time not null,
    lugarServicio varchar(150) not null,
    telefonoContacto varchar(8) not null,
    codigoEmpresa int not null,
    primary key PK_codigoServicio(codigoServicio),
	constraint FK_Servicios_Empresas foreign key (codigoEmpresa)
		references Empresas(codigoEmpresa)
);

create table Presupuesto(
	codigoPresupuesto int not null auto_increment,
    fechaSolicitud date not null,
    cantidadPresupuesto decimal(10,2) not null,
    codigoEmpresa int not null,
    primary key PK_codigoPresupuesto(codigoPresupuesto),
    constraint FK_Presupuesto_Empresas foreign key(codigoEmpresa)
		references Empresas (codigoEmpresa)
);

create table Platos(
	codigoPlato int not null auto_increment,
    cantidad int not null,
    nombrePlato varchar(50) not null,
    descripcionPlato varchar(150) not null,
	precioPlato decimal(10,2) not null,
    codigoTipoPlato int not null,
    primary key PK_codigoPlato(codigoPlato),
    constraint PK_Platos_TipoPlato foreign key(codigoTipoPlato)
		references TipoPlato(codigoTipoPlato)
);

create table Productos_has_Platos(
	Productos_codigoProducto int not null,
    codigoPlato int not null,
    codigoProducto int not null,
    primary key PK_Productos_codigoProducto(Productos_codigoProducto),
    constraint FK_Productos_has_Platos_Productos foreign key(codigoProducto)
		references Producto(codigoProducto)

);

create table Servicios_has_Platos(
	Servicios_codigoServicio int not null,
    codigoPlato int not null,
    codigoServicio int not null,
    primary key PK_Servicios_codigoServicio(Servicios_codigoServicio),
    constraint FK_Servicios_has_Platos_Servicios foreign key(Servicios_codigoServicio)
		references Producto(codigoProducto),
	constraint FK_Servicios_has_Platos_Platos foreign key (codigoPlato)
		references Platos(codigoPlato)
);


create table Servicios_has_Empleados(
	Servicios_codigoServicio int not null,
    codigoServicio int not null,
    codigoEmpleado int not null,
    fechaEvento date not null,
    horaEvento time not null,
	lugarEvento varchar(150) not null,
    primary key PK_Servicios_codigoServicio (Servicios_codigoServicio),
    constraint FK_Servicios_has_Empleados_Servicios foreign key(codigoServicio)
		references Servicios(codigoServicio),
	constraint FK_Servicios_has_Empleados_Empleados foreign key(codigoEmpleado)
		references Empleados(codigoEmpleado)
);

create table Usuario(
	codigoUsuario int not null auto_increment,
    nombreUsuario varchar(100) not null,
    apellidoUsuario varchar(100) not null,
    usuarioLogin varchar(50) not null,
    contrasena varchar(50) not null,
    primary key PK_codigoUsuario (codigoUsuario)
);

Create table Login(
	usuarioMaster varchar(50) not null,
    passwordLogin varchar(50) not null,
    primary key PK_usuarioMaster (usuarioMaster)
);

Delimiter //
	Create procedure sp_AgregarUsuario(in nombreUsuario varchar(100), in apellidoUsuario varchar(100),
		in usuarioLogin varchar(50), in contrasena varchar(50))
    Begin
		insert into Usuario(nombreUsuario, apellidoUsuario, usuarioLogin, contrasena)
        values(nombreUsuario, apellidoUsuario, usuarioLogin, contrasena);
    End// 
Delimiter ;

Delimiter //
	create procedure sp_ListarUsuarios()
		Begin
			select
				U.codigoUsuario,
				U.nombreUsuario,
				U.apellidoUsuario,
				U.usuarioLogin,
				U.contrasena
			from usuario U;
		End//
Delimiter ;

call sp_AgregarUsuario('Brener','Valladares','bvalladares','2022112');
call sp_ListarUsuarios();


-- ------------------------ Procedimientos Almacenados -----------------------------

-- -------------- Empresa --------------

-- Agregar 

Delimiter //
	Create procedure sp_AgregarEmpresa(in nombreEmpresa varchar(150), in direccion varchar(150), in telefono varchar(8))
		begin
			Insert into Empresas(nombreEmpresa, direccion, telefono)
				values(nombreEmpresa, direccion, telefono);
        End//
Delimiter ;

call sp_AgregarEmpresa('Tigo','Pradera Concepción', '98542365');
call sp_AgregarEmpresa('Intelaf','San Jose Pinula', '87452365');
call sp_AgregarEmpresa('Scandinavia Gym','Km 17.9 San José Pinula', '21985726');
call sp_AgregarEmpresa('Cinepolis', 'Boulevard Los Próceres', '22152250');
call sp_AgregarEmpresa('Ishop', 'C.C. Gran Vía Roosevelt', '23203434');
call sp_AgregarEmpresa('Adidas', 'Av. Bolivar 39-60', '24296464');

-- Listar 

Delimiter //
	Create procedure sp_ListarEmpresa()
		begin
			Select
            E.codigoEmpresa,
			E.nombreEmpresa,
            E.direccion,
            E.telefono
            From Empresas E;
        End//
Delimiter ;

call sp_ListarEmpresa();

-- Elimitar 

Delimiter //
	Create procedure sp_EliminarEmpresa(in codEmpresa int)
		begin
			delete from Empresas
            where codigoEmpresa= codEmpresa;
        End//
Delimiter ;

-- call sp_EliminarEmpresa(1);
call sp_ListarEmpresa();

-- Editar 

/Delimiter //
	create procedure sp_EditarEmpresa (in codEmpresa int, in nombreEmpresa varchar(150), in direccion varchar(150), in telefono varchar(8))
		begin
			Update Empresas E
				set E.nombreEmpresa = nombreEmpresa,
					E.direccion = direccion,
					E.telefono = telefono
				where E.codigoEmpresa = codEmpresa;
		End//
Delimiter ;

-- call sp_EditarEmpresa(2,'Little Ceaser','Condado Concepcion','98572635');
call sp_ListarEmpresa();

-- Buscar 

Delimiter //
	Create Procedure sp_BuscarEmpresa (in codEmpresa int)
		begin
			select
            E.codigoEmpresa,
            E.nombreEmpresa,
            E.direccion,
            E.telefono
            from Empresas E where E.codigoEmpresa = codEmpresa;
		End//
Delimiter ;

call sp_BuscarEmpresa (3);

-- ---------------------- Tipo Empleado -------------------

-- Agregar 

Delimiter //
	create procedure sp_AgregarTipoEmpleado(in descripcion varchar(150))
		begin
			Insert into TipoEmpleado(descripcion)
				values(descripcion);
        End//
Delimiter ;

call sp_AgregarTipoEmpleado('Mesero');
call sp_AgregarTipoEmpleado('Cocinero');
call sp_AgregarTipoEmpleado('Conserje');
call sp_AgregarTipoEmpleado('Recepcionista');
call sp_AgregarTipoEmpleado('Administrador');
call sp_AgregarTipoEmpleado('Ayudante de camarero');

-- Listar 

Delimiter //
	Create procedure sp_ListarTipoEmpleado()
		begin
			Select
			T.codigoTipoEmpleado,
            T.descripcion
            from TipoEmpleado T;
        End//
Delimiter ;

call sp_ListartipoEmpleado();

-- Elimitar 

Delimiter //
	Create procedure sp_EliminarTipoEmpleado(in codTipoEmpleado int)
		begin
			delete from TipoEmpleado
            where codigoTipoEmpleado = codTipoEmpleado;
        End//
Delimiter ;

-- call sp_EliminarTipoEmpleado(1);
call sp_ListarTipoEmpleado();

-- Editar 

Delimiter //
	create procedure sp_EditarTipoEmpleado(in codTipoEmpleado int, in descripcionT varchar(150))
		begin
			Update TipoEmpleado T
				set T.descripcion = descripcionT
				where T.codigoTipoEmpleado = codTipoEmpleado;
		End//
Delimiter ;

-- call sp_EditarTipoEmpleado(3,'Receptor');
call sp_ListarTipoEmpleado();

-- Buscar 

Delimiter //
	Create Procedure sp_BuscarTipoEmpleado (in codTipoEmpleado int)
		begin
			select
            T.codigoTipoEmpleado,
            T.descripcion
            from TipoEmpleado T where T.codigoTipoEmpleado = codTipoEmpleado;
		End//
Delimiter ;

call sp_BuscarTipoEmpleado(2);

-- --------------------- TipoPlato ------------------------

-- Agregar 

Delimiter //
	create procedure sp_AgregarTipoPlato(in descripcionTipo varchar(150))
		begin
			Insert into TipoPlato(descripcionTipo)
				values(descripcionTipo);
        End//
Delimiter ;

call sp_AgregarTipoPlato('Entradas');
call sp_AgregarTipoPlato('Desayunos');
call sp_AgregarTipoPlato('Cenas');
call sp_AgregarTipoPlato('Bebidas');
call sp_AgregarTipoPlato('Postres');

-- Listar 

Delimiter //
	Create procedure sp_ListarTipoPlato()
		begin
			Select
			TP.codigoTipoPlato,
            TP.descripcionTipo
            from TipoPlato TP;
        End//
Delimiter ;

call sp_ListarTipoPlato();

-- Elimitar 

Delimiter //
	Create procedure sp_EliminarTipoPlato(in codTipoPlato int)
		begin
			delete from TipoPlato
            where codigoTipoPlato = codTipoPlato;
        End//
Delimiter ;

-- call sp_EliminarTipoPlato(4);
call sp_ListarTipoPlato();

-- Editar 

Delimiter //
	create procedure sp_EditarTipoPlato(in codTipoPlato int, in descripcionTipoP varchar(150))
		begin
			Update TipoPlato TP
				set TP.descripcionTipo = descripcionTipoP
				where T.codigoTipoPlato = codTipoPlato;
		End//
Delimiter ;

-- call sp_EditarTipoEmpleado(5,'Clasicos');
call sp_ListarTipoEmpleado();

-- Buscar 

Delimiter //
	Create Procedure sp_BuscarTipoPlato (in codTipoPlato int)
		begin
			select
            TP.codigoTipoPlato,
            TP.descripcionTipo
            from TipoPlato TP where TP.codigoTipoPlato = codTipoPlato;
		End//
Delimiter ;

call sp_BuscarTipoEmpleado(2);

-- -------------------- Producto --------------------------

-- Agregar 

Delimiter //
	create procedure sp_AgregarProducto(in nombreProducto varchar(150), in cantidad int)
		begin
			Insert into Producto(nombreProducto, cantidad)
				values(nombreProducto, cantidad);
        End//
Delimiter ;

call sp_AgregarProducto('Jamon',3);
call sp_AgregarProducto('Tortillas de harina',100);
call sp_AgregarProducto('Papas',50);
call sp_AgregarProducto('Queso', 15);
call sp_AgregarProducto('Carne', 5);
call sp_AgregarProducto('Huevos',24);

-- Listar 

Delimiter //
	Create procedure sp_ListarProducto()
		begin
			Select
            P.codigoProducto,
			P.nombreProducto,
            P.cantidad
            from Producto P;
        End//
Delimiter ;

call sp_ListarProducto();

-- Elimitar 

Delimiter //
	Create procedure sp_EliminarProducto(in codProducto int)
		begin
			delete from Producto
            where codigoProducto = codProducto;
        End//
Delimiter ;

-- call sp_EliminarProducto(3);
call sp_ListarProducto();

-- Editar 

Delimiter //
	create procedure sp_EditarProducto(in codProducto int, in nombreProducto varchar(150), in cantidad int)
		begin
			Update Producto P
				set P.nombreProducto = nombreProducto,
					P.cantidad = cantidad
				where P.codigoProducto = codProducto;
		End//
Delimiter ;


-- call sp_EditarProducto(2,'Tocino', 8);
call sp_ListarProducto();

-- Buscar 

Delimiter //
	Create Procedure sp_BuscarProducto (in codProducto int)
		begin
			select
            P.codigoProducto,
            P.nombreProducto,
            P.cantidad
            from Producto P where P.codigoProducto = codProducto;
		End//
Delimiter ;

call sp_BuscarProducto(2);

-- -------------------- Empleados --------------------------

-- Agregar 

Delimiter //
	create procedure sp_AgregarEmpleados(in numeroEmpleado int,in apellidoEmpleado varchar(150),in nombresEmpleado varchar(150),in direccionEmpleado varchar(150),in telefonoContacto varchar(8),in gradoCocinero varchar(50), in codigoTipoEmpleado int)
		begin
			Insert into Empleados(numeroEmpleado, apellidoEmpleado, nombresEmpleado, direccionEmpleado, telefonoContacto, gradoCocinero, codigoTipoEmpleado)
				values(numeroEmpleado, apellidoEmpleado, nombresEmpleado, direccionEmpleado, telefonoContacto, gradoCocinero, codigoTipoEmpleado);
        End//
Delimiter ;


call sp_AgregarEmpleados(1,'Lopez Juarez','Critian Roberto','Ciudad de Guatemala','98652314','Mesero',1);
call sp_AgregarEmpleados(2,'Del Aguila Carrera','Shanning Cristina','Mixco','8753214','Chef',2);
call sp_AgregarEmpleados(3,'Valladares Chián','Brener Roberto','San Jose Pinula','98563214','Receptor',3);
call sp_AgregarEmpleados(4,'Fernández García','Ana María ','Villa Nueva','84751234','Administrador',4);
call sp_AgregarEmpleados(5,'Rodríguez González','Luis Pedro ','Carreta a El Salvador','74351265','Administrador',5);

-- Listar 

Delimiter //
	Create procedure sp_ListarEmpleados()
		begin
			Select
            E.codigoEmpleado,
            E.numeroEmpleado,
            E.apellidoEmpleado, 
            E.nombresEmpleado, 
            E.direccionEmpleado, 
            E.telefonoContacto, 
            E.gradoCocinero, 
            E.codigoTipoEmpleado
			from Empleados E;
        End//
Delimiter ;

call sp_ListarEmpleados();

-- Elimitar 

Delimiter //
	Create procedure sp_EliminarEmpleados(in codEmpleado int)
		begin
			delete from Empleados
            where codigoEmpleado = codEmpleado;
        End//
Delimiter ;

-- call sp_EliminarEmpleados(3);
call sp_ListarEmpleados();

-- Editar 

Delimiter //
	create procedure sp_EditarEmpleados(in codEmpleado int, in numeroEmpleado int, in apellidoEmpleado varchar(150), in nombresEmpleado varchar(150), in direccionEmpleado varchar(150),in telefonoContacto varchar(8), in gradoCocinero varchar(150), in codigoTipoEmpleado int)
		begin
			Update Empleados E
				set E.numeroEmpleado = numeroEmpleado,
					E.apellidoEmpleado = apellidoEmpleado,
                    Em.nombresEmpleado = nombresEmpleado,
                    E.direccionEmpleado = direccionEmpleado,
                    E.telefonoContacto = telefonoContacto,
                    E.gradoCocinero = gradoCocinero,
                    E.codigoTipoEmpleado = codigoTipoEmpleado
				where E.codigoEmpleado = codEmpleado;
		End//
Delimiter ;

-- call sp_EditarEmpleados(2,3,'Gonzales Robles','Diego Ruben','Villa Nueva','87562147','Chef',2);
call sp_ListarEmpleados();

-- Buscar 

Delimiter //
	Create Procedure sp_BuscarEmpleado (in codEmpleado int)
		begin
			select
            E.codigoEmpleado,
            E.numeroEmpleado, 
            E.apellidoEmpleado,
            E.nombresEmpleado, 
            E.direccionEmpleado, 
            E.telefonoContacto,
            E.gradoCocinero,
            E.codigoTipoEmpleado
            from Empleados E where E.codigoEmpleado = codEmpleado;
		End//
Delimiter ;

call sp_BuscarEmpleado(2);

-- -------------------- Servicios --------------------------

-- Agregar 

Delimiter //
	create procedure sp_AgregarServicio(in fechaServicio date, in tipoServicio varchar(150),in horaServicio time, in lugarServicio varchar(150), in telefonoContacto varchar(8),in codigoEmpresa int)
		begin
			Insert into Servicios (fechaServicio, tipoServicio, horaServicio, lugarServicio, telefonoContacto, codigoEmpresa)
				values(fechaServicio, tipoServicio, horaServicio, lugarServicio, telefonoContacto, codigoEmpresa);
        End//
Delimiter ;

call sp_AgregarServicio('2023-01-07','Cumpleaños','11:00:00','Restaurante','89571523',1);
call sp_AgregarServicio('2022-02-06','Boda','10:30:00','Xalet','98572635',2);
call sp_AgregarServicio('2022-05-15','Graduacion','12:00:00','Salon De Eventos','84562134',3);
call sp_AgregarServicio('2023-03-25','Baby Shower','03:00:00','Salon De Eventos','58651232',4);
call sp_AgregarServicio('2022-06-15','Quince Años','01:30:00','Playa','85983514',5);

-- Listar 

Delimiter //
	Create procedure sp_ListarServicio()
		begin
			Select
            S.codigoServicio,
            S.fechaServicio, 
            S.tipoServicio, 
            S.horaServicio, 
            S.lugarServicio, 
            S.telefonoContacto, 
			S.codigoEmpresa
            from Servicios S;
        End//
Delimiter ;

call sp_ListarServicio();

-- Elimitar 

Delimiter //
	Create procedure sp_EliminarServicio(in codServicio int)
		begin
			delete from Servicios
            where codigoServicio = codServicio;
        End//
Delimiter ;

-- call sp_EliminarServicio(2);
call sp_ListarServicio();

-- Editar 

Delimiter //
	create procedure sp_EditarServicio(in codServicio int,in fechaServicio date,in tipoServicio varchar(150),in horaServicio time, in lugarServicio varchar(150), in telefonoContacto varchar(8),in codigoEmpresa int)
		begin
			Update Servicios S
				set S.fechaServicio = fechaServicio,
                S.tipoServicio = tipoServicio,
                S.horaServicio = horaServicio,
                S.lugarServicio = lugarServicio,
                S.telefonoContacto = telefonoContacto,
                S.codigoEmpresa = codigoEmpresa
				where S.codigoServicio = codServicio;
		End//
Delimiter ;


-- call sp_EditarServicio(1,'2023-03-23','Primera Comunion','05:45:00','Iglesia','98571532', 2);
call sp_ListarServicio();

-- Buscar 

Delimiter //
	Create Procedure sp_BuscarServicio (in codServicio int)
		begin
			select
            S.codigoServicio, 
            S.fechaServicio, 
            S.tipoServicio, 
            S.horaServicio, 
            S.lugarServicio, 
            S.telefonoContacto, 
            S.codigoEmpresa
            from Servicios S where S.codigoServicio = codServicio;
		End//
Delimiter ;

call sp_BuscarServicio(1);

-- -------------------- Presupuesto --------------------------

-- Agregar 

Delimiter //
	create procedure sp_AgregarPresupuesto(in fechaSolicitud date, in cantidadPresupuesto decimal(10,2), in codigoEmpresa int)
		begin
			Insert into Presupuesto (fechaSolicitud, cantidadPresupuesto, codigoEmpresa)
				values(fechaSolicitud, cantidadPresupuesto, codigoEmpresa);
        End//
Delimiter ;

call sp_AgregarPresupuesto('2022-11-04',5800.00,1);
call sp_AgregarPresupuesto('2022-03-06',8000.00,2);
call sp_AgregarPresupuesto('2023-01-07',1200.00,3);
call sp_AgregarPresupuesto('2022-12-24',7600.00,4);
call sp_AgregarPresupuesto('2023-05-24',5000.00,5);


-- Listar 

Delimiter //
	Create procedure sp_ListarPresupuesto()
		begin
			Select
            P.codigoPresupuesto,
            P.fechaSolicitud,
            P.cantidadPresupuesto, 
            P.codigoEmpresa
            from Presupuesto P;
        End//
Delimiter ;

call sp_ListarPresupuesto();

-- Elimitar 

Delimiter //
	Create procedure sp_EliminarPresupuesto(in codPresupuesto int)
		begin
			delete from Presupuesto
            where codigoPresupuesto = codPresupuesto;
        End//
Delimiter ;

-- call sp_EliminarPresupuesto(2);
call sp_ListarPresupuesto();

-- Editar 

Delimiter //
	create procedure sp_EditarPresupuesto(in codPresupuesto int, in fechaSolicitud date,in cantidadPresupuesto decimal)
		begin
			Update Presupuesto P
				set 
                P.fechaSolicitud = fechaSolicitud, 
                P.cantidadPresupuesto = cantidadPresupuesto
				where P.codigoPresupuesto = codPresupuesto;
		End//
Delimiter ;


--  call sp_EditarPresupuesto(1,'2022-08-25',5000.00);
call sp_ListarPresupuesto();

-- Buscar 

Delimiter //
	Create Procedure sp_BuscarPresupuesto (in codPresupuesto int)
		begin
			select
            P.codigoPresupuesto,
            P.fechaSolicitud, 
            P.cantidadPresupuesto,
            P.codigoEmpresa
            from Presupuesto P where P.codigoPresupuesto = codPresupuesto;
		End//
Delimiter ;

call sp_BuscarPresupuesto(1);

-- -------------------- Platos --------------------------

-- Agregar 

Delimiter //
	create procedure sp_AgregarPlatos(in cantidad int ,in nombrePlato varchar(50), in descripcionPlato varchar(150),in precioPlato decimal(10,2),in codigoTipoPlato int)
		begin
			Insert into Platos (cantidad, nombrePlato, descripcionPlato, precioPlato, codigoTipoPlato)
				values(cantidad, nombrePlato, descripcionPlato, precioPlato, codigoTipoPlato);
        End//
Delimiter ;

call sp_AgregarPlatos(30,'Lasagna','Contiene tres tipos de queso',60.00,1);
call sp_AgregarPlatos(150,'Tacos al pastor','carne de cerdo marinado en salsa de Chile',30.00,2);
call sp_AgregarPlatos(200,'Ceviche','con camaron, congrejo y pulpo',50.00,3);
call sp_AgregarPlatos(70,'Burritos','Con carne molida y queso cheddar',25.00,4);



-- Listar 

Delimiter //
	Create procedure sp_ListarPlatos()
		begin
			Select
            P.codigoPlato,
            P.cantidad, 
            P.nombrePlato,
            P.descripcionPlato, 
            P.precioPlato,
            P.codigoTipoPlato
            from Platos P;
        End//
Delimiter ;

call sp_ListarPlatos();

-- Elimitar 

Delimiter //
	Create procedure sp_EliminarPlatos(in codPlatos int)
		begin
			delete from Platos
            where codigoPlato = codPlatos;
        End//
Delimiter ;

-- call sp_EliminarPlatos(2);
call sp_ListarPlatos();

-- Editar 

Delimiter //
	create procedure sp_EditarPlatos(in codPlatos int, in cantidad int,in nombrePlato varchar(50),in descripcion varchar(150),in precioPlato decimal(10,2))
		begin
			Update Platos P
				set 
                P.cantidad = cantidad,
                P.nombrePlato = nombrePlato,
                P.descripcionPlato = nombrePlato,
                P.precioPlato = precioPlato
				where P.codigoPlato = codPlatos;
		End//
Delimiter ;


-- call sp_EditarPlatos(1,50,'Churrasco','Libra y medio de pullazo',95.00);
call sp_ListarPlatos();

-- Buscar 

Delimiter //
	Create Procedure sp_BuscarPlatos (in codPlatos int)
		begin
			select
				P.codigoPlato,
				P.cantidad,
                P.nombrePlato, 
                P.descripcionPlato,
                P.precioPlato,
                P.codigoTipoPlato
            from Platos P where P.codigoPlato = codPlatos;
		End//
Delimiter ;

call sp_BuscarPlatos(1);

-- -------------------- Productos_Has_Platos --------------------------

-- Agregar 

Delimiter //
	create procedure sp_AgregarProductoHasPlatos(in Productos_codigoProducto int, in codigoPlato int, in codigoProducto int)
		begin
			Insert into Productos_Has_Platos(Productos_codigoProducto,codigoPlato, codigoProducto)
				values(Productos_codigoProducto,codigoPlato, codigoProducto);
        End//
Delimiter ;

call sp_AgregarProductoHasPlatos(1,1,2);
call sp_AgregarProductoHasPlatos(2,1,1);


-- Listar 

Delimiter //
	Create procedure sp_ListarProductoHasPlatos()
		begin
			Select
            P.Productos_codigoProducto, 
            P.codigoPlato, 
            P.codigoProducto
            from Productos_Has_Platos P;
        End//
Delimiter ;

call sp_ListarProductoHasPlatos();

-- Elimitar 

Delimiter //
	Create procedure sp_EliminarProductoHasPlatos(in codProducto int)
		begin
			delete from Productos_Has_Platos
            where Productos_codigoProducto = codProducto;
        End//
Delimiter ;

-- call sp_EliminarProductoHasPlatos(2);
call sp_ListarProductoHasPlatos();

-- Editar 

Delimiter //
	create procedure sp_EditarProductoHasPlatos(in codProducto int, in codigoPlato int)
		begin
			Update Productos_Has_Platos P
				set 
                P.codigoPlato = codigoPlato
				where P.Productos_codigoProducto = codProducto;
		End//
Delimiter ;


-- call sp_EditarProductoHasPlatos(1,2);
call sp_ListarProductoHasPlatos();

-- Buscar 

Delimiter //
	Create Procedure sp_BuscarProductoHasPlatos (in codProducto int)
		begin
			select
            P.Productos_codigoProducto,
            P.codigoPlato,
            P.codigoProducto
            from Productos_Has_Platos P where P.Productos_codigoProducto = codProducto;
		End//
Delimiter ;

call sp_BuscarProductoHasPlatos(1);

-- -------------------- Servicios_Has_Platos --------------------------

-- Agregar 

Delimiter //
	create procedure sp_AgregarServicioHasPlatos(in Servicios_codigoServicio int, in codigoPlato int, in codigoServicio int)
		begin
			Insert into Servicios_Has_Platos(Servicios_codigoServicio,codigoPlato, codigoServicio)
				values(Servicios_codigoServicio,codigoPlato, codigoServicio);
        End//
Delimiter ;


call sp_AgregarServicioHasPlatos(1,1,1);
call sp_AgregarServicioHasPlatos(2,1,1);


-- Listar 

Delimiter //
	Create procedure sp_ListarServicioHasPlatos()
		begin
			Select
            S.Servicios_codigoServicio, 
            S.codigoPlato, 
            S.codigoServicio
            from Servicios_Has_Platos S;
        End//
Delimiter ;

call sp_ListarServicioHasPlatos();

-- Elimitar 

Delimiter //
	Create procedure sp_EliminarServicioHasPlatos(in codServicio int)
		begin
			delete from Servicios_Has_Platos
            where Servicios_codigoServicio = codServicio;
        End//
Delimiter ;

-- call sp_EliminarServicioHasPlatos(2);
call sp_ListarServicioHasPlatos();

-- Editar 

Delimiter //
	create procedure sp_EditarServicioHasPlatos(in codServicio int, in codigoPlato int)
		begin
			Update Servicios_Has_Platos S
				set 
                S.codigoPlato = codigoPlato
				where S.Servicios_codigoServicio = codServicio;
		End//
Delimiter ;


-- call sp_EditarServicioHasPlatos(1,1);
call sp_ListarServicioHasPlatos();

-- Buscar 

Delimiter //
	Create Procedure sp_BuscarServicioHasPlatos (in codServicio int)
		begin
			select
            S.Servicios_codigoServicio,
            S.codigoPlato,
            S.codigoServicio
            from Servicios_Has_Platos S where S.Servicios_codigoServicio = codServicio;
		End//
Delimiter ;

call sp_BuscarServicioHasPlatos(1);

-- -------------------- Servicios_has_Empleados --------------------------

-- Agregar 

Delimiter //
	create procedure sp_AgregarServicioHasEmpleados(in Servicios_codigoServicio int, in codigoServicio int, in codigoEmpleado int, in fechaEvento date, in horaEvento time, lugarEvento varchar(150))
		begin
			Insert into Servicios_has_Empleados(Servicios_codigoServicio, codigoServicio, codigoEmpleado, fechaEvento, horaEvento, lugarEvento)
				values(Servicios_codigoServicio, codigoServicio, codigoEmpleado, fechaEvento, horaEvento, lugarEvento);
        End//
Delimiter ;


call sp_AgregarServicioHasEmpleados(1,1,2,'2023-04-01','11:30:00', 'Pradera Concepcion');
call sp_AgregarServicioHasEmpleados(2,1,1,'2022-07-01','22:00:00','Ay Carmela');


-- Listar 

Delimiter //
	Create procedure sp_ListarServicioHasEmpleados()
		begin
			Select
            S.Servicios_codigoServicio, 
            S.codigoServicio,
            S.codigoEmpleado,
            S.fechaEvento,
            S.horaEvento,
            S.lugarEvento
            from Servicios_has_Empleados S;
        End//
Delimiter ;

call sp_ListarServicioHasEmpleados();

-- Elimitar 

Delimiter //
	Create procedure sp_EliminarServicioHasEmpleados(in codServicioE int)
		begin
			delete from Servicios_has_Empleados
            where Servicios_codigoServicio = codServicioE;
        End//
Delimiter ;

-- call sp_EliminarServicioHasEmpleados(2);
call sp_ListarServicioHasEmpleados();

-- Editar 

Delimiter //
	create procedure sp_EditarServicioHasEmpleados(in codServicioE int, in codigoServicio int, in codigoEmpleado int, in fechaEvento date, in horaEvento time, in lugarEvento varchar(150))
		begin
			Update Servicios_has_Empleados S
				set 
                S.codigoServicio = codigoServicio,
                S.codigoEmpleado = codigoEmpleado,
                S.fechaEvento = fechaEvento,
                S.horaEvento = horaEvento,
                S.lugarEvento = lugarEvento
				where S.Servicios_codigoServicio = codServicioE;
		End//
Delimiter ;


-- call sp_EditarServicioHasEmpleados(1,1,2,'2023-01-07','20:30:00','San Jose Pinula');
call sp_ListarServicioHasEmpleados();

-- Buscar 

Delimiter //
	Create Procedure sp_BuscarServicioHasEmpleados (in codServicioE int)
		begin
			select
            S.Servicios_codigoServicio,
            S.codigoServicio,
			S.codigoEmpleado,
			S.fechaEvento ,
			S.horaEvento,
            S.lugarEvento
            from Servicios_has_Empleados S where S.Servicios_codigoServicio = codServicioE;
		End//
Delimiter ;

call sp_BuscarServicioHasEmpleados(1);


Delimiter //
	Create procedure sp_ReporteGeneral(in cod int)
		Begin
				Select E.codigoEmpresa,
				E.nombreEmpresa,
				E.direccion,
				E.telefono,
				P.cantidadPresupuesto,
				P.fechaSolicitud,
				S.fechaServicio,
				S.tipoServicio,
				S.horaServicio,
				S.lugarServicio,
				Pl.cantidad, 
				Pl.nombrePlato,
				Pl.descripcionPlato, 
				Pl.precioPlato,
				Tp.descripcionTipo,
				Em.numeroEmpleado, 
				Em.apellidoEmpleado, 
				Em.nombresEmpleado, 
				Em.direccionEmpleado,
				Em.telefonoContacto,
				Em.gradoCocinero,
				Te.descripcion,
				Pr.nombreProducto, 
				Pr.cantidad
				FROM Empresas E
						inner join Presupuesto P on P.codigoEmpresa = E.codigoEmpresa 
						Inner Join Servicios S on S.codigoEmpresa = E.codigoEmpresa
						Inner Join Servicios_has_Platos ShP on Shp.codigoServicio = S.codigoServicio
						Inner Join Platos Pl on Pl.codigoPlato = ShP.codigoPlato
						Inner Join TipoPlato Tp on Tp.codigoTipoPlato = Pl.codigoTipoPlato
						Inner Join Servicios_has_Empleados ShE on ShE.codigoServicio = S.codigoServicio
						Inner Join Empleados Em on Em.codigoEmpleado = ShE.codigoEmpleado
						Inner Join TipoEmpleado Te on Te.codigoTipoEmpleado = Em.codigoTipoEmpleado
						Inner Join Productos_has_Platos PhP on PhP.codigoPlato = Pl.codigoPlato
						Inner Join Producto Pr on Pr.codigoProducto = PhP.codigoProducto
						where E.codigoEmpresa = cod;
        End//
 Delimiter ;
call sp_ReporteGeneral(1);

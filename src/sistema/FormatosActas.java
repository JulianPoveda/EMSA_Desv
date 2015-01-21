package sistema;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

//import ws_connect.DownLoadParametros;
import ws_connect.UpLoadActaImpresa;
//import zebra_printer.Label;

import clases.ClassCensoCarga;
import clases.ClassConfiguracion;
import clases.ClassInSolicitudes;

import android.content.ContentValues;
import android.content.Context;
	
public class FormatosActas {
	//Variables para el manejo de la API para impresoras zebra
	private Context 					context;
	private String 						_folderAplicacion;
	private ArrayList<ContentValues> 	_infTabla		= new ArrayList<ContentValues>();
	private ContentValues 				_infRegistro1	= new ContentValues();
	private String 						_nombreTecnico;
	private String 						_cedulaTecnico;
	
	private ClassCensoCarga		FcnCensoCarga;
	private ClassConfiguracion 	FcnConfig;
	private ClassInSolicitudes	FcnSolicitudes;
	private Archivos 			ImpArchivos;
	//private Bluetooth 			MnBt;
	private SQLite 				ImpSQL;
	//private Label				FcnZebra;
	private Zebra_QL420plus		FcnZebra;
	
	private String Impresora = null;
	private boolean _copiaSistema;
	   
    DecimalFormat decimales = new DecimalFormat("0.000"); 
	
	public FormatosActas(Context context, String FolderAplicacion, boolean _copiaSistema){
		this.context = context;
		this._folderAplicacion 	= FolderAplicacion;
		this._copiaSistema 		= _copiaSistema;
		
		ImpSQL = new SQLite(this.context,this._folderAplicacion);
		
		this.FcnCensoCarga	= new ClassCensoCarga(this.context, this._folderAplicacion);
		this.FcnConfig		= new ClassConfiguracion(this.context, this._folderAplicacion);
		this.FcnSolicitudes = new ClassInSolicitudes(this.context, this._folderAplicacion);
		this.ImpArchivos	= new Archivos(this.context,this._folderAplicacion, 10);
		this.Impresora 		= ImpSQL.StrSelectShieldWhere("amd_configuracion", "valor", "item='impresora'");
	    
		//MnBt 	= new Bluetooth(this.context);
		ImpSQL 	= new SQLite(this.context, this._folderAplicacion);
		FcnZebra= new Zebra_QL420plus(600, 40, 5, 5, 100, _copiaSistema);
	}
	
	
	
	public void FormatoPrueba(){	
		this._cedulaTecnico = this.ImpSQL.StrSelectShieldWhere("amd_configuracion", "valor", "item='cedula_tecnico'");
		this._nombreTecnico = this.ImpSQL.StrSelectShieldWhere("amd_configuracion", "valor", "item='nombre_tecnico'");
		
		FcnZebra.clearInformacion();
		FcnZebra.WrTitulo("ELECTRIFICADORA DEL META E.S.P.", 0, 1);
		FcnZebra.DrawImage("LOGOEMSA.PCX", 30, 25);
		FcnZebra.WrTitulo("ACTA DE REVISION RUTINARIA DE FACTURACION Y SERVICIO AL CLIENTE", 0, 2);
		
		FcnZebra.WrLabel("ñÑáéíóúÁÉÍÓÚ", "xxxxxxxxxxxxxxx", 150, 0, 1);
		
		FcnZebra.WrLabel("N.          ", "xxxxxxxxxxxxxxx", 150, 0, 1);
		FcnZebra.WrLabel("Solicitud:  ", "---------------", 150, 0, 1);
		FcnZebra.WrLabel("Codigo:     ", "123456789", 150, 0, 1);
		FcnZebra.WrLabel("", "Villavicencio", 200, 0, 1);
		FcnZebra.WrLabel("Contratista:", "SYPELC LTDA.", 150, 0, 1.2);
		
		//MnBt.IntentPrint(this.Impresora,FcnZebra.getDoLabel());
		FcnZebra.printLabel(this.Impresora);
		FcnZebra.resetEtiqueta();
	}
	
	/********************************************************************************************************************
	 * 
	 * @param _solicitud
	 * @param _tipoImpresion
	 * @param _copiaImpresion
	 * @param CedulaTecnico
	 *******************************************************************************************************************/
	public void FormatoDesviacion(String _solicitud, String _tipoImpresion, int _copiaImpresion){	
		this._cedulaTecnico = this.ImpSQL.StrSelectShieldWhere("amd_configuracion", "valor", "item='cedula_tecnico'");
		this._nombreTecnico = this.ImpSQL.StrSelectShieldWhere("amd_configuracion", "valor", "item='nombre_tecnico'");
		
		FcnZebra.clearInformacion();
		FcnZebra.WrTitulo("ELECTRIFICADORA DEL META E.S.P.", 0, 1);
		FcnZebra.DrawImage("LOGOEMSA.PCX", 30, 25);
		FcnZebra.WrTitulo("ACTA DE REVISION RUTINARIA DE FACTURACION Y SERVICIO AL CLIENTE", 0, 2);
		 
		 
		 
		 /********************************Validaciones necesarias para el encabezado del acta**************************************/
		FcnZebra.WrLabel("N.          ", this.FcnSolicitudes.getDependencia(_solicitud)+_solicitud+this.FcnConfig.getEquipo()+this.FcnSolicitudes.getIdSerial(_solicitud), 150, 0, 1);
		FcnZebra.WrLabel("Solicitud:  ", this.FcnSolicitudes.getDependencia(_solicitud)+_solicitud, 150, 0, 1);
		FcnZebra.WrLabel("Codigo:     ", ImpSQL.StrSelectShieldWhere("in_ordenes_trabajo", "cuenta", "solicitud='"+_solicitud+"'"), 150, 0, 1);
		FcnZebra.WrLabel("", ImpSQL.StrSelectShieldWhere("vista_in_ordenes_trabajo", "nombre_municipio", "solicitud='"+_solicitud+"'"), 200, 0, 1);
		FcnZebra.WrLabel("Contratista:", "SYPELC LTDA.", 150, 0, 1.2);
		
		
		this._infRegistro1.clear();
		this._infRegistro1 = ImpSQL.SelectDataRegistro("vista_dig_actas", "dia,mes,anno,hora,suscriptor,nom_enterado,doc_enterado,tipo_enterado,doc_testigo,nom_testigo", "solicitud='"+_solicitud+"'");
		FcnZebra.JustInformation("A los "+this._infRegistro1.getAsString("dia")+" dias del mes "+this._infRegistro1.getAsString("mes")+" de "+this._infRegistro1.getAsString("anno")+", siendo las "+this._infRegistro1.getAsString("hora")+", se hacen presentes en el inmueble de: "+this._infRegistro1.getAsString("suscriptor")+" los representantes de EMSA E.S.P. "+this._nombreTecnico+" con Cod/C.C: "+this._cedulaTecnico+" y en presencia del senor(a): "+this._infRegistro1.getAsString("nom_enterado")+" con cedula "+this._infRegistro1.getAsString("doc_enterado")+" en calidad de "+this._infRegistro1.getAsString("tipo_enterado")+", con el fin de efectuar una revision de los equipos de medida e instalaciones electricas del inmueble con el codigo indicado. Habiendose identificado los empleados/contratistas informan al usuario que de acuerdo al contrato de servicios publicos con condiciones uniformes vigente su derecho a solicitar asesoria y/o participacion de un tecnico particular, o de cualquier persona para que sirva de testigo en el proceso de revision. Sin embargo, si transcurre un plazo de 15 minutos sin hacerse presente se hara la revision sin su presencia, el cliente/usuario hace uso de su derecho Si( ) No( ). Transcurridos 15 minutos , procede a hacer la revision, con los siguientes resultados:", 10, 2, 1);
		
		/***************************************************************Datos del suscriptor****************************************************/
		FcnZebra.WrSubTitulo("DATOS GENERALES DEL SUSCRIPTOR",10,1,1);
		this._infRegistro1 = this.ImpSQL.SelectDataRegistro("vista_in_ordenes_trabajo", "suscriptor,direccion,cuenta,clase_servicio,nombre_municipio,estrato,nodo,marca,serie,carga_contratada", "solicitud='"+_solicitud+"'");
		FcnZebra.WrLabel("SUSCRIPTOR: ", this._infRegistro1.getAsString("suscriptor"),10,0,1);
		FcnZebra.WrLabel("DIRECCION: ", this._infRegistro1.getAsString("direccion"),10,0,1); 
		FcnZebra.WrLabel("CUENTA: ", this._infRegistro1.getAsString("cuenta"),10,0,1);
		FcnZebra.WrLabel("SERVICIO: ", this._infRegistro1.getAsString("clase_servicio"),10,0,1);
		FcnZebra.WrLabel("MUNICIPIO: ", this._infRegistro1.getAsString("nombre_municipio"),10,0,1);
		FcnZebra.WrLabel("ESTRATO: ", this._infRegistro1.getAsString("estrato"),10,0,1);
		FcnZebra.WrLabel("NODO: ", this._infRegistro1.getAsString("nodo"),10,0,1);
		FcnZebra.WrLabel("MARCA: ",this._infRegistro1.getAsString("marca"),10,0,1);
		FcnZebra.WrLabel("SERIE: ",this._infRegistro1.getAsString("serie"),10,0,1);
		FcnZebra.WrLabel("CARGA CONTRATADA: ",this._infRegistro1.getAsString("carga_contratada"),10,0,1);
		
		/**************************************************Datos del suscriptor y equipo de medida********************************************/
		FcnZebra.WrSubTitulo("DATOS DEL SUSCRIPTOR Y EQUIPO DE MEDIDA",10,1,1);		
		FcnZebra.WrSubTitulo("MEDIDOR",10,0,1);
		this._infRegistro1 = this.ImpSQL.SelectDataRegistro("dig_contador", "marca,serie,lectura1,lectura2,tipo", "solicitud='"+_solicitud+"'");
		if(this._infRegistro1.size()>0){
			FcnZebra.WrLabel("Marca:    ", this._infRegistro1.getAsString("marca"),20,0,1);
			FcnZebra.WrLabel("Serie:    ", this._infRegistro1.getAsString("serie"),20,0,1); 
			FcnZebra.WrLabel("Lectura1: ", this._infRegistro1.getAsString("lectura1"),20,0,1);
			FcnZebra.WrLabel("Lectura2: ", this._infRegistro1.getAsString("lectura2"),20,0,1);
			FcnZebra.WrLabel("tipo:     ", this._infRegistro1.getAsString("tipo"),20,0,1);
		}
		
		
		FcnZebra.WrSubTitulo("ACOMETIDAS",10,1,1);
		this._infRegistro1 = this.ImpSQL.SelectDataRegistro("dig_acometida", "tipo_ingreso,conductor,tipo,calibre,clase,fases,longitud", "solicitud='"+_solicitud+"'");
		if(this._infRegistro1.size()>0){
			FcnZebra.WrLabel("", this._infRegistro1.getAsString("tipo_ingreso"),20,0,1);
			FcnZebra.WrLabel("Tipo:    ", 	this._infRegistro1.getAsString("tipo"),30,0,0); 
			FcnZebra.WrLabel("Calibre: ", 	this._infRegistro1.getAsString("calibre"),320,0,1);
			FcnZebra.WrLabel("Longitud: ", 	this._infRegistro1.getAsString("longitud"),30,0,0);
			FcnZebra.WrLabel("Fases: ", 	this._infRegistro1.getAsString("fases"),320,0,1);
			FcnZebra.WrLabel("Clase:     ", this._infRegistro1.getAsString("clase"),30,0,1);
		}
		
		
		FcnZebra.WrSubTitulo("SELLOS",10,1,0);
		this._infTabla = this.ImpSQL.SelectData("dig_sellos", "serie,tipo_sello,ubicacion", "solicitud='"+_solicitud+"' AND tipo_ingreso='Retirado'");
		if(this._infTabla.size()>0){
			FcnZebra.WrLabel("", "RETIRADO",20,1,1);
			for(int i=0;i<this._infTabla.size();i++){
				this._infRegistro1 = this._infTabla.get(i);
				FcnZebra.WrLabel("", this._infRegistro1.getAsString("serie")+" - "+this._infRegistro1.getAsString("tipo_sello")+this._infRegistro1.getAsString("ubicacion"),30,0,1);
			}
		}
		
		this._infTabla = this.ImpSQL.SelectData("dig_sellos", "serie,tipo_sello,ubicacion", "solicitud='"+_solicitud+"' AND tipo_ingreso='Existente'");
		if(this._infTabla.size()>0){
			FcnZebra.WrLabel("", "EXISTENTE",20,1,1);
			for(int i=0;i<this._infTabla.size();i++){
				this._infRegistro1 = this._infTabla.get(i);
				FcnZebra.WrLabel("", this._infRegistro1.getAsString("serie")+" - "+this._infRegistro1.getAsString("tipo_sello")+this._infRegistro1.getAsString("ubicacion"),30,0,1);
			}
		}
		
		this._infTabla = this.ImpSQL.SelectData("dig_sellos", "serie,tipo_sello,ubicacion", "solicitud='"+_solicitud+"' AND tipo_ingreso='Instalado'");
		if(this._infTabla.size()>0){
			FcnZebra.WrLabel("", "INSTALADO",20,1,1);
			for(int i=0;i<this._infTabla.size();i++){
				this._infRegistro1 = this._infTabla.get(i);
				FcnZebra.WrLabel("", this._infRegistro1.getAsString("serie")+" - "+this._infRegistro1.getAsString("tipo_sello")+this._infRegistro1.getAsString("ubicacion"),30,0,1);
			}
		}
		
		
		this._infTabla = this.FcnCensoCarga.getElementosCensoResidencial(_solicitud);
		FcnZebra.WrSubTitulo("CENSO VATIOS (W) RESIDENCIAL",10,1,1);
		FcnZebra.WrLabel("ELEMENTO", "",10,0,0);
		FcnZebra.WrLabel("CARGA", "",300,0,0);
		FcnZebra.WrLabel("CAP(W)", "",400,0,0);
		FcnZebra.WrLabel("CNT", "",500,0,1);
		
		for(int i=0;i<this._infTabla.size();i++){
			this._infRegistro1 = this._infTabla.get(i);
			FcnZebra.WrLabel(this._infRegistro1.getAsString("descripcion"),"",10,0,0);
			FcnZebra.WrLabel(this._infRegistro1.getAsString("carga"),"",300,0,0);
			FcnZebra.WrLabel(this._infRegistro1.getAsString("vatios"),"",400,0,0);
			FcnZebra.WrLabel(this._infRegistro1.getAsString("cantidad"),"",500,0,1);
		}
		FcnZebra.WrLabel("REGISTRADA: ", this.FcnCensoCarga.getTotalCensoResidencialRegistrada(_solicitud)+"",30,0,0);
		FcnZebra.WrLabel("DIRECTA: ", this.FcnCensoCarga.getTotalCensoResidencialDirecta(_solicitud)+"",300,0,1);
		FcnZebra.WrLabel("TOTAL: ", this.FcnCensoCarga.getTotalCensoResidencial(_solicitud)+"",10,0,1);
		
		
		this._infTabla = this.FcnCensoCarga.getElementosCensoNoResidencial(_solicitud);
		FcnZebra.WrSubTitulo("CENSO VATIOS (W) NO RESIDENCIAL",10,1,1);
		FcnZebra.WrLabel("ELEMENTO", "",10,0,0);
		FcnZebra.WrLabel("CARGA", "",300,0,0);
		FcnZebra.WrLabel("CAP(W)", "",400,0,0);
		FcnZebra.WrLabel("CNT", "",500,0,1);
		
		for(int i=0;i<this._infTabla.size();i++){
			this._infRegistro1 = this._infTabla.get(i);
			FcnZebra.WrLabel(this._infRegistro1.getAsString("descripcion"),"", 10,0,0);
			FcnZebra.WrLabel(this._infRegistro1.getAsString("carga"),"", 300,0,0);
			FcnZebra.WrLabel(this._infRegistro1.getAsString("vatios"),"", 400,0,0);
			FcnZebra.WrLabel(this._infRegistro1.getAsString("cantidad"),"", 500,0,1);
		}
		FcnZebra.WrLabel("REGISTRADA: ", this.FcnCensoCarga.getTotalCensoNoResidencialRegistrada(_solicitud)+"",30,0,0);
		FcnZebra.WrLabel("DIRECTA: ", this.FcnCensoCarga.getTotalCensoResidencialDirecta(_solicitud)+"",300,0,1);
		FcnZebra.WrLabel("TOTAL: ", this.FcnCensoCarga.getTotalCensoNoResidencial(_solicitud)+"",10,0,1);
		
		
		FcnZebra.WrLabel("RESUMEN CENSO DE CARGA(W)", "",10,1,1);
		FcnZebra.WrLabel("REGISTRADA: ", this.FcnCensoCarga.getTotalCensoRegistrada(_solicitud)+"",10,0,0);
		FcnZebra.WrLabel("DIRECTA: ", this.FcnCensoCarga.getTotalCensoDirecta(_solicitud)+"",300,0,1);
		FcnZebra.WrLabel("TOTAL: ", this.FcnCensoCarga.getTotalCenso(_solicitud)+"",10,0,1);
		
		if(this.FcnSolicitudes.getCargaContratada(_solicitud)<this.FcnCensoCarga.getTotalCenso(_solicitud)){
			FcnZebra.JustInformation("SE ENCUENTRA AUMENTO DE CARGA POR ENCIMA DE LA CONTRATADA EN "+((this.FcnCensoCarga.getTotalCenso(_solicitud)-this.FcnSolicitudes.getCargaContratada(_solicitud))/1000)+" KW",10,0,1);
		}
		
		this._infTabla = this.ImpSQL.SelectData("dig_observaciones", "tipo_observacion, observacion", "solicitud='"+_solicitud+"'");
		if(this._infTabla.size()>0){
			FcnZebra.WrLabel("OBSERVACIONES", "",10,1,1);
			for(int i=0;i<this._infTabla.size();i++){
				this._infRegistro1 = this._infTabla.get(i);
				FcnZebra.WrLabel(this._infRegistro1.getAsString("tipo_observacion"), "",20,0,1);
				FcnZebra.JustInformation(this._infRegistro1.getAsString("observacion"), 20,0,2);
			}
		}
		
		
		/**Datos Acta  y adecuaciones a realizar**/
		FcnZebra.WrLabel("DATOS ACTA", "",10,1,1);
		this._infRegistro1 = this.ImpSQL.SelectDataRegistro("dig_datos_actas", "irregularidades,prueba_rozamiento,prueba_frenado,prueba_vacio,familias,fotos,electricista,clase_medidor,ubicacion_medidor,aplomado,registrador,telefono,porcentaje_no_res", "solicitud='"+_solicitud+"'");
		FcnZebra.WrLabel("Kw no residencial:      ",(this.FcnCensoCarga.getTotalCensoNoResidencial(_solicitud)/1000)+"",20,0,1);
		FcnZebra.WrLabel("% no residencial:       ",this._infRegistro1.getAsString("porcentaje_no_res"),20,0,1);
		FcnZebra.WrLabel("Medidor_aplomado:       ",this._infRegistro1.getAsString("aplomado"),20,0,1);
		FcnZebra.WrLabel("Medidor int/ext:        ",this._infRegistro1.getAsString("ubicacion_medidor"),20,0,1);
		FcnZebra.WrLabel("Registrador:            ",this._infRegistro1.getAsString("registrador"),20,0,1);
		FcnZebra.WrLabel("Medidor Elect/Inducc:   ",this._infRegistro1.getAsString("clase_medidor"),20,0,1);
		FcnZebra.WrLabel("Asistio Electricista:   ",this._infRegistro1.getAsString("electricista"),20,0,1);
		FcnZebra.WrLabel("Telefono:               ",this._infRegistro1.getAsString("telefono"),20,0,1);
		FcnZebra.WrLabel("Familias:               ",this._infRegistro1.getAsString("familias"),20,0,1);
		FcnZebra.WrLabel("Fotos:                  ",this._infRegistro1.getAsString("fotos"),20,0,1);
		FcnZebra.WrLabel("Prueba Giro en Vacio:   ",this._infRegistro1.getAsString("prueba_vacio"),20,0,1);
		FcnZebra.WrLabel("Prueba Medidor Se Frena:",this._infRegistro1.getAsString("prueba_frenado"),20,0,1);
		FcnZebra.WrLabel("Irregularidades:        ",this._infRegistro1.getAsString("irregularidades"),20,0,1);
		FcnZebra.WrLabel("Prueba Rozamiento:      ",this._infRegistro1.getAsString("prueba_rozamiento"),20,0,1);
		
		
		FcnZebra.WrLabel("ADECUACIONES A REALIZAR", "",10,1,1);
		this._infRegistro1 = this.ImpSQL.SelectDataRegistro("dig_adecuaciones", "suspension,tubo,armario,soporte,tierra,acometida,caja,medidor,otros", "solicitud='"+_solicitud+"'");
		FcnZebra.WrLabel("Cambiar o instalar medidor:            ",this._infRegistro1.getAsString("medidor"),20,0,1);
		FcnZebra.WrLabel("Cambiar o instalar acometida:          ",this._infRegistro1.getAsString("acometida"),20,0,1);
		FcnZebra.WrLabel("Cambiar o instalar puesta a tierra:    ",this._infRegistro1.getAsString("tierra"),20,0,1);
		FcnZebra.WrLabel("Adecuar o cambiar soporte de acometida:",this._infRegistro1.getAsString("soporte"),20,0,1);
		FcnZebra.WrLabel("Instalar armario de medidor:           ",this._infRegistro1.getAsString("armario"),20,0,1);
		FcnZebra.WrLabel("Cambiar o instalartubo de acometida:   ",this._infRegistro1.getAsString("tubo"),20,0,1);
		FcnZebra.WrLabel("Suspension:                            ",this._infRegistro1.getAsString("suspension"),20,0,1);
		FcnZebra.WrLabel("Cambiar o instalar caja:               ",this._infRegistro1.getAsString("caja"),20,0,1);
		FcnZebra.WrLabel("Otros:     ",this._infRegistro1.getAsString("otros"),20,0,1);
		
		/**Irregularidades**/
		this._infTabla = this.ImpSQL.SelectData("dig_irregularidades", "irregularidad", "solicitud='"+_solicitud+"'");
		if(this._infTabla.size()>0){
			FcnZebra.WrLabel("IRREGULARIDADES", "",10,1,1);
			for(int i=0;i<this._infTabla.size();i++){
				this._infRegistro1 = this._infTabla.get(i);
				FcnZebra.JustInformation(this._infRegistro1.getAsString("irregularidad"), 20,0,1);
			}
		}
		
		/********************************************************Impresion de leyenda al final del acta********************************************/	
		FcnZebra.JustInformation("En caso de detectarse irregularidad(es) esta acta se constituye en acta de irregularidades, por lo cual procede como tal ante el cliente o usuario del servicio de energia electrica.",10, 2, 1);
		FcnZebra.JustInformation("Señor usuario en caso de no estar de acuerdo con el presente resultado de esta revision, puede presentar descargos que justifiquen la presencia de las anomalias detectadas por escrito al momento de firmar el acta o dentro de los cinco (5) dias habiles siguientes, en comunicacion dirigida al area de control de energia de la empresa, citando el No. de esta acta y radicando su comunicacion en la oficina de atencion al cliente mas cercana, la no presentacion de los descargos indica a la empresa que el usuario (suscriptor) acepta el concepto tecnico emitido. Los abajo firmantes reconocen haber leido y aceptado el contenido de esta acta y mediante su firma la dan por levantada. EL USO INDEBIDO DEL SERVICIO, LA ADULTERACION O MANIPULACION DEL EQUIPO DE MEDIDA SE CONSTITUYE EN DELITO DE DEFRAUDACION DE FUIDOS (articulo 256 del codigo penal).",10, 1, 1);
		 
		FcnZebra.WrLabel("", ImpSQL.StrSelectShieldWhere("dig_actas", "nom_enterado", "solicitud='"+_solicitud+"'"), 10, 5, 0);
		FcnZebra.WrLabel("", ImpSQL.StrSelectShieldWhere("dig_actas", "nom_testigo", "solicitud='"+_solicitud+"'"), 300, 0, 1);
		FcnZebra.WrLabel("CC.", ImpSQL.StrSelectShieldWhere("dig_actas", "doc_enterado", "solicitud='"+_solicitud+"'"), 10, 0, 0);
		FcnZebra.WrLabel("CC.", ImpSQL.StrSelectShieldWhere("dig_actas", "doc_testigo", "solicitud='"+_solicitud+"'"), 300, 0, 1);
		FcnZebra.WrLabel("", "Usuario", 10, 0, 0);
		FcnZebra.WrLabel("", "Testigo", 300, 0, 1);
		FcnZebra.WrLabel("",  this._nombreTecnico, 10, 6, 1);
		FcnZebra.WrLabel("CC.", this._cedulaTecnico, 10, 0, 1);
		FcnZebra.WrLabel("", "Empleado y/o Contratista", 10, 0, 1);
		
		FcnZebra.JustInformation("NO PAGAR NI REALIZAR NEGOCIACIONES CON EL OPERARIO POR NINGUN CONCEPTO. DENUNCIE CUALQUIER IRREGULARIDAD AL TELEFONO"+" 6614000", 10, 1, 1);	
		
		switch(_copiaImpresion){
			case 1:
				FcnZebra.WrLabel("", "Original Empresa", 10, 0, 1);
				break;
			case 2:
				FcnZebra.WrLabel("", "Copia Usuario", 10, 0, 1);
				break;
			case 3:
				FcnZebra.WrLabel("", "Copia Archivo", 10, 0, 1);
				break;
		}
		//FcnZebra.WrLabel("",ImpSQL.StrSelectShieldWhere("amd_param_sistema", "valor", "codigo='NPDA'")+" - "+ DT.GetDateTimeHora(), 10, 0, 1);
		FcnZebra.WrLabel("","Supervisor/Interventoria:________________________________________", 10, 0, 1);
		
		if((_copiaSistema)&&((_copiaImpresion==1)||(_copiaImpresion==2))){
			if(!this.ImpArchivos.ExistFolderOrFile(this._folderAplicacion+File.separator+_solicitud)){
				this.ImpArchivos.MakeDirectory(_solicitud);
			}
			int num_impresion = ImpSQL.IntSelectShieldWhere("dig_impresiones_inf", "max(id_impresion)","solicitud='"+_solicitud+"'")+1;
			this.ImpArchivos.DoFile(_solicitud, _tipoImpresion+"_"+num_impresion,FcnZebra.getInfArchivo());
			
			this._infRegistro1.clear();
			this._infRegistro1.put("solicitud", _solicitud);
			this._infRegistro1.put("id_impresion", num_impresion);
			this._infRegistro1.put("nombre_archivo", _tipoImpresion+"_"+num_impresion);
			this.ImpSQL.InsertRegistro("dig_impresiones_inf", this._infRegistro1);
		}
		FcnZebra.printLabel(this.Impresora);
		
		/*MnBt.IntentPrint(this.Impresora,FcnZebra.getDoLabel());*/
		/*printerConnection = new BluetoothConnection(this.Impresora);
		try {
            printerConnection.open();
        } catch (ConnectionException e) {
        	try {
				printerConnection.close();
			} catch (ConnectionException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	//disconnect();
        }*/
		//ZebraPrinter printer = null;
		
		FcnZebra.resetEtiqueta();
		new UpLoadActaImpresa(this.context, this._folderAplicacion).execute();
	}
}

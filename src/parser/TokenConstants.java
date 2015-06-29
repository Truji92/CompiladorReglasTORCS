package parser;//------------------------------------------------------------------//
//                        COPYRIGHT NOTICE                          //
//------------------------------------------------------------------//
// Copyright (c) 2008, Francisco Jos� Moreno Velo                   //
// All rights reserved.                                             //
//                                                                  //
// Redistribution and use in source and binary forms, with or       //
// without modification, are permitted provided that the following  //
// conditions are met:                                              //
//                                                                  //
// * Redistributions of source code must retain the above copyright //
//   notice, this list of conditions and the following disclaimer.  // 
//                                                                  //
// * Redistributions in binary form must reproduce the above        // 
//   copyright notice, this list of conditions and the following    // 
//   disclaimer in the documentation and/or other materials         // 
//   provided with the distribution.                                //
//                                                                  //
// * Neither the name of the University of Huelva nor the names of  //
//   its contributors may be used to endorse or promote products    //
//   derived from this software without specific prior written      // 
//   permission.                                                    //
//                                                                  //
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND           // 
// CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,      // 
// INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF         // 
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE         // 
// DISCLAIMED. IN NO EVENT SHALL THE COPRIGHT OWNER OR CONTRIBUTORS //
// BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,         // 
// EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED  //
// TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,    //
// DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND   // 
// ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT          //
// LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING   //
// IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF   //
// THE POSSIBILITY OF SUCH DAMAGE.                                  //
//------------------------------------------------------------------//

//------------------------------------------------------------------//
//                      Universidad de Huelva                       //
//           Departamento de Tecnolog�as de la Informaci�n          //
//   �rea de Ciencias de la Computaci�n e Inteligencia Artificial   //
//------------------------------------------------------------------//
//                     PROCESADORES DE LENGUAJE                     //
//------------------------------------------------------------------//
//                                                                  //
//          Compilador del lenguaje Tinto [Versi�n 0.0]             //
//                                                                  //
//------------------------------------------------------------------//

/**
 * Interfaz que define los c�digos de las diferentes categor�as l�xicas
 *  
 * * @author Francisco Jos� Moreno Velo
 *
 */
public interface TokenConstants {

	/**
	 * Final de fichero
	 */
	public int EOF = 0;
	
	//--------------------------------------------------------------//
	// Palabras clave
	//--------------------------------------------------------------//

    public int PERCEPTION = 1;
    public int ACTION = 2;
	public int RULES = 3;
	public int INNER = 4;
	public int INT = 5;
	public int DOUBLE = 6;
	public int BOOLEAN = 7;
	public int TRUE = 8;
	public int FALSE = 9;
    public int SPEED = 10;
    public int ANGLE = 11;
    public int POSITION = 12;
    public int RPM = 13;
    public int SENSOR_0 = 14;
    public int SENSOR_1 = 15;
    public int SENSOR_2 = 16;
    public int SENSOR_3 = 17;
    public int SENSOR_4 = 18;
    public int SENSOR_5 = 19;
    public int SENSOR_6 = 20;
    public int SENSOR_7 = 21;
    public int SENSOR_8 = 22;
    public int SENSOR_9 = 23;
    public int SENSOR_10 = 24;
    public int SENSOR_11 = 25;
    public int SENSOR_12 = 26;
    public int SENSOR_13 = 27;
    public int SENSOR_14 = 28;
    public int SENSOR_15 = 29;
    public int SENSOR_16 = 30;
    public int SENSOR_17 = 31;
    public int SENSOR_18 = 32;
    public int GEAR = 33;
    public int ACCELERATE = 34;
    public int BRAKE = 35;
    public int STEERING = 36;
    public int IF = 37;
    public int ELSE = 38;
    public int WHILE = 39;

	//--------------------------------------------------------------//
	// Identificadores y literales
	//--------------------------------------------------------------//

	public int IDENTIFIER = 40;
	public int INTEGER_LITERAL = 41;
	public int DOUBLE_LITERAL = 42;

	//--------------------------------------------------------------//
	// Separadores
	//--------------------------------------------------------------//

	public int LPAREN = 43;
	public int RPAREN = 44;
	public int LBRACE = 45;
	public int RBRACE = 46;
	public int SEMICOLON = 47;
	public int COMMA = 48;
    public int ARROW = 64;
	
	//--------------------------------------------------------------//
	// Operadores
	//--------------------------------------------------------------//

    /** <- */
	public int ASSIGN = 49;

    /** = */
	public int EQ = 50;

    /**
	 * Menor "<"
	 */
	public int LT = 51;

    /**
	 * Menor o igual "<="
	 */
	public int LE = 52;
	
	/**
	 * Mayor ">"
	 */
	public int GT = 53;
	
	/**
	 * Mayor o igual ">="
	 */
	public int GE = 54;
	
	/**
	 * Distinto "<>"
	 */
	public int NE = 55;
	
	/**
	 * Or "|"
	 */
	public int OR = 56;
	
	/**
	 * and "&"
	 */
	public int AND = 57;
	
	/**
	 * Not "!"
	 */
	public int NOT = 58;
	
	/**
	 * Suma "+"
	 */
	public int PLUS = 59;
	
	/**
	 * Resta "-"
	 */
	public int MINUS = 60;
	
	/**
	 * Producto "*"
	 */
	public int PROD = 61;
	
	/**
	 * Divisi�n "/"
	 */
	public int DIV = 62;
	
	/**
	 * M�dulo "%"
	 */
	public int MOD = 63;
}

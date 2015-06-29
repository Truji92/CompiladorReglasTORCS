//------------------------------------------------------------------//
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
 * Clase que describe un excepci�n sint�ctica
 * 
 * @author Francisco Jos� Moreno Velo
 */
public class SintaxException extends Exception implements TokenConstants {

	/**
	 * Constante asignada al objeto serializable
	 */
	private static final long serialVersionUID = 20080002L;

	/**
	 * Mensaje de error
	 */
	private String msg;
	
	/**
	 * Constructor con un solo tipo esperado
	 * @param token
	 * @param expected
	 */
	public SintaxException(Token token, int expected) {
		this.msg = "Sintax exception at row "+token.getRow();
		msg += ", column "+token.getColumn()+".\n";
		msg += "  Found "+token.getLexeme()+"\n";
		msg += "  while expecting "+getLexemeForKind(expected)+".\n";
	}
	
	/**
	 * Constructor con una lista de tipos esperados
	 * @param token
	 * @param expected
	 */
	public SintaxException(Token token, int[] expected) {
		this.msg = "Sintax exception at row "+token.getRow();
		msg += ", column "+token.getColumn()+".\n";
		msg += "  Found "+token.getLexeme()+"\n";
		msg += "  while expecting one of\n";
		for(int i=0; i<expected.length; i++) {
			msg += "    "+getLexemeForKind(expected[i])+"\n";
		}
	}
	
	/**
	 * Obtiene el mensaje de error
	 */
	public String toString() {
		return this.msg;
	}
	
	/**
	 * Descripci�n del token
	 * @param kind
	 * @return
	 */
	private String getLexemeForKind(int kind) {
		switch(kind) {
            case PERCEPTION: return "perception";
            case ACTION: return "action";
            case RULES: return "rules";
            case INNER: return "inner";
            case SPEED: return "speed";
            case ANGLE: return "angle";
            case POSITION: return "position";
            case RPM: return "rpm";
            case SENSOR_0: return "sensor0";
            case SENSOR_1: return "sensor1";
            case SENSOR_2: return "sensor2";
            case SENSOR_3: return "sensor3";
            case SENSOR_4: return "sensor4";
            case SENSOR_5: return "sensor5";
            case SENSOR_6: return "sensor6";
            case SENSOR_7: return "sensor7";
            case SENSOR_8: return "sensor8";
            case SENSOR_9: return "sensor9";
            case SENSOR_10: return "sensor10";
            case SENSOR_11: return "sensor11";
            case SENSOR_12: return "sensor12";
            case SENSOR_13: return "sensor13";
            case SENSOR_14: return "sensor14";
            case SENSOR_15: return "sensor15";
            case SENSOR_16: return "sensor16";
            case SENSOR_17: return "sensor17";
            case SENSOR_18: return "sensor18";
            case GEAR: return "gear";
            case ACCELERATE: return "accelerate";
            case BRAKE: return "brake";
            case STEERING: return "steering";
            case BOOLEAN: return "boolean";
			case ELSE:  return "else";
			case FALSE: return "false";
			case IF: return "if";
			case INT: return "int";
			case TRUE: return "true";
			case WHILE: return "while";
			case IDENTIFIER: return "IDENTIFIER";
			case INTEGER_LITERAL: return "INTEGER_LITERAL";
            case DOUBLE_LITERAL: return "DOUBLE_LITERAL";
			case LPAREN: return "(";
			case RPAREN: return ")";
			case LBRACE: return "{";
			case RBRACE: return "}";
			case SEMICOLON: return ";";
			case COMMA: return "COMMA";
			case ASSIGN: return "<-";
			case EQ: return "=";
			case LE: return "<=";
			case LT: return "<";
			case GE: return ">=";
			case GT: return ">";
			case NE: return "<>";
			case OR: return "|";
			case AND: return "&";
			case NOT: return "!";
			case PLUS: return "+";
			case MINUS: return "-";
			case PROD: return "*";
			case DIV: return "/";
			case MOD: return "%";
            case ARROW: return "->";
			default: return "";
		}
	}
}

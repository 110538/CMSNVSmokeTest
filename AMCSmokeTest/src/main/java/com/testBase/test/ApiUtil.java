package com.testBase.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.testng.Assert;

public class ApiUtil extends TestBase {
	
	
	public JSONObject jsonObj;
	public static JSONObject mpJsonObj;
	public JSONObject jsonObjEmpty = new JSONObject();
	public static JSONObject wopJsonObj;
	public static JSONObject sourceJsonObj;
	public static JSONObject metadata;
	public String apirequest = "no request";
	public StringBuffer textView = new StringBuffer();
	public StringBuffer textView_1 = new StringBuffer();
	public StringBuffer textView_2 = new StringBuffer();
	public StringBuffer sourceText = new StringBuffer();
	public String text2 = null;
	public String text1 = null;
	public String text = null;
	public int testline;
	public static String accessToken = "no value";
	public String token = "no value";
	public String paramText;
	public String paramName;
	public String pName;
	public ArrayList<String> paramvalues = new ArrayList<String>();
	public static ArrayList<String> wopParamvalues = new ArrayList<String>();
	public static ArrayList<String> mpParamvalues = new ArrayList<String>();
	public static ArrayList<String> SourceParamvalues = new ArrayList<String>();
	public ArrayList<NameValuePair> postParameters;
	public List<NameValuePair> nameValuePairs;
	public String[] apirequests;
	public static String RLID;
	public static int finalApiType;
	public static int finalApiCount;
	public String excelSheet;
	public static String externalFileName;
	public static String[][] dataBook;
	public String path = System.getProperty("user.dir");
	public XSSFWorkbook workbook;
	public String jsonPrettyPrintString = null;
	
	
	ExcelUtils excelUtils = new ExcelUtils();
	
	
	// ========Method For API Get Call==============

		public JSONObject getapiExecute(String lineNumbers, String sheetname, String RLID) throws Exception {
			clearJSon();
			textView.delete(0, textView.length());

			textView_1.delete(0, textView_1.length());

			// Local Variables
			String[] cTypes;

			String[] hTypes;

			//logStep("Started executing API Testing");

			String[] lines;

			if (lineNumbers.contains(",")) {

				lines = lineNumbers.split(",");

			} else {

				lines = lineNumbers.split(" ");
			}

			for (int lin = 1; lin <= lines.length; lin++) {

				int lineNumber = Integer.parseInt(lines[lin - 1]);

				testline = lineNumber;

				excelUtils.readingExcel(sheetname);

				//logStep("Verify Api Type is Get  or Post");

				if (excelUtils.excelData[lineNumber][1].toString().equalsIgnoreCase("GET")) {

					//logStep("Given url is Get API continue execution");

					HttpClient client = HttpClients.createDefault();

					//logStep("Given API is : " + excelUtils.excelData[lineNumber][0]);

					HttpGet request = new HttpGet(excelUtils.excelData[lineNumber][0]);

					String cType =excelUtils.excelData[lineNumber][2].toString();

					String hType =excelUtils.excelData[lineNumber][3].toString().replace("%s", RLID);

					if (cType.contains(",")) {

						cTypes = cType.split(",");

						hTypes = hType.split(",");
					} else {

						cTypes = cType.split(" ");

						hTypes = hType.split(" ");
					}
					for (int i = 0; i <= cTypes.length - 1; i++) {

						if (cTypes[i].trim().contains("Authorization")) {

							//logStep("Using Access tokena as Bearer : " + accessToken);

							//logStep("Headers are: : " + cTypes[i] + "------" + hTypes[i] + " " + accessToken);

							request.addHeader(cTypes[i], hTypes[i] + " " + accessToken);
						} else if (cTypes[i].trim().contains("auth2")) {

							//logStep("Using Access tokena as Bearer : " + token);

							//logStep("Headers are: : " + cTypes[i] + "------" + hTypes[i] + " " + token);

							request.addHeader(cTypes[i], hTypes[i] + " " + token);
						} else {

							//logStep("Headers are: : " + cTypes[i] + "------" + hTypes[i]);

							request.addHeader(cTypes[i], hTypes[i]);
						}
						System.out.println("Headers are: : " + cTypes[i] + "------" + hTypes[i]);
					}

					HttpResponse response = client.execute(request);

					//logStep("Execution Completed and reading response now");

					BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

					//logStep("Verify Status code in response");

					if (response.getStatusLine().getStatusCode() != 200) {

						RuntimeException error = new RuntimeException(

								"Failed : HTTP error code : " + response.getStatusLine().getStatusCode());

						System.out.println(error);

						//logStep(error.toString());

						throw new RuntimeException(
								"Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
					} else {

						String line = "";

						while ((line = rd.readLine()) != null) {

							textView_1.append(line);

						}
						if (textView_1.toString().contains("xml")) {

							text1 = convertxmltoJson(textView_1.toString());

						} else if (!textView_1.toString().contains("xml")) {

							jsonObj = new JSONObject();

							text1 = textView_1.toString();

							if (text1.startsWith("[")) {

								textView_1.deleteCharAt(0);

								textView_1.deleteCharAt(textView_1.length() - 1);

								text1 = textView_1.toString();
							}

							System.out.println("response  is : =========" + text1.toString());
						}

						jsonObj = new JSONObject(text1.toString());

						System.out.println(jsonObj.toString());

						if (sheetname.contains("APIMP")) {

							mpJsonObj = jsonObj;

						} else if (sheetname.contains("WOPAPI")) {

							wopJsonObj = jsonObj;

						}

					}

				}
			}

			// Reads the WOPData based the WOP sheetname
			// Reads the MPData bases on the MP sheetname

			if (finalApiType == finalApiCount) {

				if (sheetname.contains("WOP")) {

					sheetname = "wopData";

					sourceJsonObj = wopJsonObj;

				} else if (sheetname.contains("MP")) {

					sheetname = "mpData";

					sourceJsonObj = mpJsonObj;
				}

				excelUtils.readingexcelFiles(sheetname);

				dataBook = excelUtils.getDataFromExcel(ExcelUtils.workbook, sheetname);

				XSSFSheet sheet = ExcelUtils.workbook.getSheet(sheetname);

				int rowCount = sheet.getPhysicalNumberOfRows();

				for (int i = 1; i <= rowCount - 1; i++) {

					// Calling the getValue Method to Read the WOPData
					// Calling the getValue Method to Read the MPData

					getValue(sheetname, dataBook[i][3].toString());

				}
			}

			return jsonObj;
		}

		// Clearing the JSon Object

		public void clearJSon() {

			jsonObj = jsonObjEmpty;

			textView_1 = textView_1.delete(0, textView_1.length());

			paramvalues.clear();
		}

		// Method For Clearing the JSon Object

		public void clearJSonObj() {

			mpJsonObj = jsonObjEmpty;

			wopJsonObj = jsonObjEmpty;

		}

		// ========Method For API Post Call===========

		public JSONObject testhttpclientforPost(int lineNumber, String sheetname, String RLID) throws Exception {

			excelUtils.readingExcel(sheetname);

			clearJSon();

			//logStep("Get the details from Excel");

			String reqExcel =excelUtils.excelData[lineNumber][4].toString();

			addparamvaluesinRequest(lineNumber, excelUtils.excelData[lineNumber][4].toString(), sheetname, RLID);

			//logStep("Http Client service is invoked");

			HttpClient client = HttpClients.createDefault();

			// Executing the API POST Method using URL , Content Type, Header , XML Request

			HttpPost post = new HttpPost(excelUtils.excelData[lineNumber][0].toString());

			String[] cTypes;

			String[] hTypes;
			try {
				if (apirequest.equals("no request")) {

					apirequest = excelUtils.excelData[lineNumber][4].toString();

					//logStep("apirequest is  : --" + apirequest);

				} else if (apirequest.contains("&") && !reqExcel.equalsIgnoreCase("externalFile")) {

					//logStep("apirequest is  : --" + apirequest);

					nameValuePairs = new ArrayList<NameValuePair>();

					postParameters = new ArrayList<NameValuePair>();

					String[] params;

					apirequests = apirequest.split("&");

					for (String param : apirequests) {

						if (param.contains("=")) {

							params = param.split("=");

							nameValuePairs.add(new BasicNameValuePair(params[0], params[1]));

							postParameters.add(new BasicNameValuePair(params[0], params[1]));

						}
					}
					//logStep("Executing request with http Post method");

					post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				} else {

					//logStep("apirequest is  : --" + apirequest);

					StringEntity entity = new StringEntity(apirequest);

					post.setEntity(entity);

					String cType = excelUtils.excelData[lineNumber][2].toString();

					String hType =excelUtils.excelData[lineNumber][3].toString();

					if (cType.contains(",")) {

						cTypes = cType.split(",");

						hTypes = hType.split(",");

					} else {

						cTypes = cType.split(" ");

						hTypes = hType.split(" ");
					}
					for (int i = 0; i <= cTypes.length - 1; i++) {

						//logStep("Using Access tokena as Bearer : " + token);

						if (cTypes[i].trim().contains("Authorization")) {

							//logStep("Using Access tokena as Bearer : " + accessToken);

							//logStep("Headers are: : " + cTypes[i] + "------" + hTypes[i] + " " + accessToken);

							System.out.println(cTypes[i] + "," + hTypes[i] + "-- " + accessToken);

							post.addHeader(cTypes[i], hTypes[i] + " " + accessToken);

						} else if (cTypes[i].trim().contains("auth2")) {

							//logStep("Using Access tokena as Bearer : " + token);

							//logStep("Headers are: : " + cTypes[i] + "------" + hTypes[i] + " " + token);

							post.addHeader(cTypes[i], hTypes[i] + " " + token);

						} else {

							//logStep("Headers are: : " + cTypes[i] + "------" + hTypes[i]);

							post.addHeader(cTypes[i], hTypes[i]);
						}
					}

					//logStep("Executing post API with Http Post method");
				}

				HttpResponse response = client.execute(post);

				//logStep("Response is : " + response);

				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

				// Prints the API Response Status
				if (response.getStatusLine().getStatusCode() != 200) {

					RuntimeException error = new RuntimeException(
							"Failed : HTTP error code : " + response.getStatusLine().getStatusCode());

					System.out.println(error);

					//logStep(error.toString());

					throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());

				} else {

					//logStep("API Executed Successully : ");

					String line = "";

					while ((line = rd.readLine()) != null) {

						textView_1.append(line);

					}
					if (textView_1.toString().contains("xml")) {

						text1 = convertxmltoJson(textView_1.toString());

					} else if (!textView_1.toString().contains("xml")) {

						jsonObj = new JSONObject();

						text1 = textView_1.toString();

						if (text1.startsWith("[")) {

							textView_1.deleteCharAt(0);

							textView_1.deleteCharAt(textView_1.length() - 1);

							text1 = textView_1.toString();
						}

						System.out.println("response  is : =========" + textView_1.toString());
					}
					jsonObj = new JSONObject(text1);

					System.out.println(jsonObj.toString());

					if (sheetname.contains("MP")) {

						mpJsonObj = jsonObj;

					} else if (sheetname.contains("WOP")) {

						wopJsonObj = jsonObj;
					}
				}

				logStep("API Executed Successfully");

				System.out.println("Api Executed successfully");

				//logStep(jsonObj.toString());

			} catch (IOException e) {

				e.printStackTrace();
			}

			// Reads the WOPData based the WOP sheetname
			// Reads the MPData bases on the MP sheetname

			if (finalApiType == finalApiCount) {

				if (sheetname.contains("WOP")) {

					sheetname = "wopData";

					sourceJsonObj = wopJsonObj;

				} else if (sheetname.contains("MP")) {

					sheetname = "mpData";

					sourceJsonObj = mpJsonObj;
				}

				excelUtils.readingexcelFiles(sheetname);

				dataBook = excelUtils.getDataFromExcel(ExcelUtils.workbook, sheetname);

				XSSFSheet sheet =ExcelUtils.workbook.getSheet(sheetname);

				int rowCount = sheet.getPhysicalNumberOfRows();

				for (int i = 1; i <= rowCount - 1; i++) {

					getValue(sheetname, dataBook[i][3].toString());

				}
			}

			return jsonObj;
		}

		// Convert XML response to JSON Response

		public String convertxmltoJson(String xmlString) {

			try {

				JSONObject xmlJSONObj = XML.toJSONObject(xmlString);

				jsonPrettyPrintString = xmlJSONObj.toString();

				System.out.println(jsonPrettyPrintString);

			} catch (JSONException je) {

				System.out.println(je.toString());
			}

			return jsonPrettyPrintString;
		}

		// Method For File Reader

		public void readFile(String path) throws IOException {

			File file = new File(path);

			StringBuilder sb = new StringBuilder();

			try (BufferedReader br = new BufferedReader(new FileReader(file))) {

				String line = "";

				while ((line = br.readLine()) != null) {

					sb.append(line);

					sb.append("\n");
				}
			}

			apirequest = convertxmltoJson(sb.toString());

		}

		// Add Parameter Values in Request Of Next API Call.

		public void addparamvaluesinRequest(int lineNumber, String jsonValue, String sheetName, String RLID)
				throws IOException {

			excelUtils.readingExcel(excelSheet);

			apirequest = excelUtils.excelData[lineNumber][4].toString().replaceAll("%s", RLID);

			if (apirequest.equalsIgnoreCase("externalFile")) {

				if (externalFileName.contains("Media_Asset")) {

					readFile(path + "\\Media_Asset.xml");

				} else if (externalFileName.contains("Component_Data")) {

					readFile(path + "\\Component_Data.xml");

				} else {

					System.out.println("Api Request from Excel");

					//logStep("Checkes request details from Excel");

					try {

						//JSONParser parser = new JSONParser();

						JSONObject jObject = new JSONObject(apirequest);

						logStep("Request in Json formate");

						JSONObject maintag = (JSONObject) jObject.getJSONObject("request");

						metadata = null;

						String[] metaValues;

						if (jsonValue.contains(",")) {

							metaValues = jsonValue.split(",");

						} else {

							metaValues = jsonValue.split(" ");
						}

						//logStep("Try ing to add values to request");

						for (int i = 0; i <= metaValues.length - 1; i++) {

							if (accessToken.equals("no value") || token.equals("no value")) {

								System.out.println("No Token values are present");

							} else {

								if (jsonValue.contains(metaValues[i])) {

									metadata = maintag.getJSONObject("metadata").put(metaValues[i], accessToken);

								} else if (jsonValue.contains("token")) {

									metadata = maintag.getJSONObject("metadata").put(metaValues[i], token);
								}
							}
						}

						apirequest = jObject.toString();

						//logStep("Edited request is : ----" + apirequest);

					} catch (Exception e) {

						logStep("request is not in JSON formate : --" + apirequest);

					}
				}
			}

			System.out.println("metadata -- :" + apirequest);
		}

		// Reads the JSON response

		public void readJson(int lineNumber, String getValue) {

			if (getValue.equalsIgnoreCase("access_token")) {

				accessToken = (String) jsonObj.get("access_token");

				//logStep(getValue + " value is :" + accessToken);

				System.out.println(getValue + " value is :" + accessToken);

			} else if (getValue.equalsIgnoreCase("token")) {

				token = (String) jsonObj.get("token");

				//logStep(getValue + " value is :" + token);

				System.out.println(getValue + " value is :" + token);
			}
		}

		// Reading JsonType From Excel Sheet
		// Reading API Parameters From Excel Sheet

		public void getValue(String JsonType, String Param) throws Exception, IOException {

			excelUtils.readingexcelFiles(JsonType);
			
			
			String[][] dataBook = excelUtils.getDataFromExcel(ExcelUtils.workbook, JsonType);

			//logger("Started reading Responses from " + JsonType + " API ");

			String paramvalue;

			String jsontypes = null;

			String[] jTypes;

			if (JsonType.equalsIgnoreCase("MPData")) {

				jsonObj = mpJsonObj;

			} else if (JsonType.equalsIgnoreCase("WOPData")) {

				jsonObj = wopJsonObj;

			} else if (JsonType.equalsIgnoreCase("source")) {

				jsonObj =DatabaseUtil.sourceJsonObj;
			}

			XSSFSheet sheet = ExcelUtils.workbook.getSheet(JsonType);

			int rowCount = sheet.getPhysicalNumberOfRows();

			for (int excellist = 1; excellist <= rowCount - 1; excellist++) {

				paramName = dataBook[excellist][3].toString();

				paramvalue = dataBook[excellist][0].toString();

				if (Param.equalsIgnoreCase(paramName)) {

					//logger("Validating " + Param + " param name in " + JsonType + " API");

					pName = paramName;

					jsontypes = dataBook[excellist][2].toString();

					if (jsontypes.contains(",")) {

						jTypes = jsontypes.split(",");

					} else {

						jTypes = jsontypes.split("  ");
					}

					for (int i = 0; i <= jTypes.length - 1; i++) {

						if (jTypes[i].toString().contains("direct")) {

							paramText = jsonObj.get(paramvalue).toString();

							break;

						} else {

							Object obj_reult = jsonObj.get(jTypes[i].toString());

							JSONArray Obj_resultArray = null;

							if (obj_reult instanceof JSONObject) {

								jsonObj = (JSONObject) obj_reult;

							} else {

								Obj_resultArray = (JSONArray) jsonObj.get(jTypes[i].toString());

								i = i + 1;

								jsonObj = (JSONObject) Obj_resultArray.get(Integer.parseInt(jTypes[i].toString()));

							}
						}
					}

					paramText = jsonObj.get(paramvalue).toString();

					if (paramName.equalsIgnoreCase("RLA_RL_ASSET_ID")) {

						RLID = paramText;
					}

					int loopcount = 0;

					if (JsonType.equals("source")) {

						SourceParamvalues.add(paramText);

						// logger("Source Param values are ------ : " + SourceParamvalues);

					} else if (JsonType.equalsIgnoreCase("wopdata")) {

						for (String source : SourceParamvalues) {

							if(paramText.equals("")) {
								paramText="null";
							}
							
							if (paramText.equals(source)) {

								logStep("CMSNV " + paramName + " [ " + source + " ] is Matched with WOP " + paramName
										+ " [ " + paramText + " ] ");
								//logger("CMSNV " + paramName + " ( " + source + " ) is Matched with WOP " + paramName + " ( "
								//+ paramText + " )");

								System.out.println("CMSNV " + paramName + " [ " + source + " ] is Matched with WOP "
										+ paramName + " [ " + paramText + " ] ");
								break;

							} else {

								loopcount = loopcount + 1;

								if (loopcount == SourceParamvalues.size()) {

									logStep("CMSNV " + paramName + " [ " + source + " ] is not Matched with WOP "
											+ paramName + " [ " + paramText + " ] ");

									Assert.fail("CMSNV " + paramName + " [ " + source + " ] is not Matched with WOP "
											+ paramName + " [ " + paramText + " ] ");
								}
							}
						}

						wopParamvalues.add(paramText);

					} else if (JsonType.equalsIgnoreCase("mpdata")) {

						for (String source : SourceParamvalues) {

							if (paramText.equals(source)) {

						logStep("CMSNV " + paramName + " [ " + source + " ] is Matched with MP " + paramName + " [ "
										+ paramText + " ] ");

								//logger("CMSNV " + paramName + " ( " + source + " ) is Matched with MP " + paramName + " ( "
								//	+ paramText + " )");

								System.out.println("CMSNV " + paramName + " [ " + source + " ] is Matched with MP "
										+ paramName + " [ " + paramText + " ] ");
								break;

							} else {

								loopcount = loopcount + 1;

								if (loopcount == SourceParamvalues.size()) {

							logStep("CMSNV " + paramName + " [ " + source + " ] is not Matched with MP " + paramName
											+ " [ " + paramText + " ] ");

									Assert.fail("CMSNV " + paramName + " [ " + source + " ] is not Matched with MP "
											+ paramName + " [ " + paramText + " ] ");
								}
							}
						}

						mpParamvalues.add(paramText);

					} else {

						paramvalues.add(paramText);

					}

					break;

				}
			}

		}

}

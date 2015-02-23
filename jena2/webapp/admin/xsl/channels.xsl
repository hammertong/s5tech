<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0"
		xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match="/">

   <table cellspacing="0" cellpadding="5" style="font-size: 10px;">

	<tr class="dataHead">
		<td align="right" class="dataHeadTopLeft" style="width: 30px;">nr.</td>
	   	<td align="right" class="dataHead" style="width: 120px;">coordinator</td>
	   	<td align="right" class="dataHead" style="width: 80px;">canale</td>		
		<td align="right" class="dataHead" style="display: none;"></td>		
		<td align="right" class="dataHead" style="display: none;"></td>		
		<td align="right" class="dataHead" style="width: 220px;">lista canali IEEE 802.15.4 consentiti</td>
		<td align="right" class="dataHead" style="width: 60px;">comandi</td>		
		<td align="right" class="dataHeadTopRight" style="width: 180px;">indirizzo di porta</td>
	</tr>

    <xsl:for-each select="coordinators/entry">

	    <tr valign="top">

		  <td class="dataLeft" align="right">
	      	<b><xsl:value-of select="position()"/></b>
	      </td>

	      <td class="data" align="right">
			<xsl:choose>
				<xsl:when test="mac-address = 'null'">
					non-pervenuto
				</xsl:when>
				<xsl:otherwise>	      			
					<xsl:value-of select="mac-address" />
				</xsl:otherwise>
			</xsl:choose>      				
		  </td>	   
		  
		  <td style="display: none;">
			<xsl:value-of select="online" />
		  </td>
		  
		  <td style="display: none;">
			<xsl:value-of select="channelslist" />
		  </td>
		  
		  <xsl:choose>
		  
			<xsl:when test="online = 'true'">
			
				  <td class="data" align="right">
					<xsl:choose>
						<xsl:when test="channel = 'null'">
							in scansione...
						</xsl:when>
						<xsl:otherwise>	      			
							<xsl:value-of select="channel" />
						</xsl:otherwise>
					</xsl:choose>
				  </td>
				
				  <td class="data" align="right">
					<table style="font-size: 12px; border: 1px blue solid; background: white;">
						<tr>
							<td>								
								<input type="checkbox" value="11"></input>11
								<input type="checkbox" value="12"></input>12
								<input type="checkbox" value="13"></input>13
								<input type="checkbox" value="14"></input>14
							</td>
						</tr>
						<tr>
							<td>
								<input type="checkbox" value="15"></input>15
								<input type="checkbox" value="16"></input>16
								<input type="checkbox" value="17"></input>17
								<input type="checkbox" value="18"></input>18
							</td>
						</tr>
						<tr>
							<td>
								<input type="checkbox" value="19"></input>19
								<input type="checkbox" value="20"></input>20
								<input type="checkbox" value="21"></input>21
								<input type="checkbox" value="22"></input>22
							</td>
						</tr>
						<tr>
							<td>
								<input type="checkbox" value="23"></input>23
								<input type="checkbox" value="24"></input>24
								<input type="checkbox" value="25"></input>25
								<input type="checkbox" value="26"></input>26
							</td>
						</tr>
					</table>
				  </td>
				  
				  <td class="data" align="right">
					<table cellpaddng="10" cellspacing="5" border="0">
						<tr>
							<td>
								<input type="button" value="set channels" onclick="setchannels(this)" style="width: 120px; text-align: center"></input>			
							</td>
							<td align="right">
								<img src="images/channel.gif"></img>			
							</td>
						</tr>
						<tr>
							<td>
								<input type="button" value="set time" onclick="settime(this);" style="width: 120px; text-align: center"></input>	
							</td>
							<td align="right">
								<img src="images/clock.gif"></img>			
							</td>
						</tr>		
						<tr>
							<td>
								<input type="button" value="resubmit" onclick="retryPriceUpdate(this);" style="width: 120px; text-align: center"></input>	
							</td>
							<td align="right">
								<img src="images/euro.gif"></img>			
							</td>
						</tr>						
					</table>
				  </td>	 
				  
			</xsl:when>
			
			<xsl:otherwise>	
				<td class="data" colspan="3" align="right">
					<xsl:choose>
						<xsl:when test="last-offline-time = 'never'">
							offline (mai online)
						</xsl:when>
						<xsl:otherwise>	      			
							offline da <xsl:value-of select="last-offline-time" />	
						</xsl:otherwise>
					</xsl:choose>					
				</td>
			</xsl:otherwise>
			
		  </xsl:choose>
		  
	      <td class="dataRight" align="right">		  
			  <xsl:choose>		  
				<xsl:when test="online = 'true'">
					<xsl:value-of select="port-url" />	
				</xsl:when>			
				<xsl:otherwise>	
					<!-- 
					<input type="button" value="rimuovi network" onclick="removenetwork();" style="width: 120px; text-align: center"></input>
					<div style="display: none;"><xsl:value-of select="port-url" /></div>
					 -->
					 <xsl:value-of select="port-url" />
					 (offline) 
				</xsl:otherwise>
			  </xsl:choose>				
          </td>	

	    </tr>

    </xsl:for-each>

	<tr>
		<td class="dataHeadBottomLeft" colspan="5" align="left">
			Totale: <b style="color: darkBlue;"><xsl:value-of select="count(coordinators//entry)" /></b> 
			accentratori
		</td>
		<td class="dataHeadBottomRight" align="right" style="color: white;">-</td>
	</tr>

  </table>

<br/>

<i style="font-size: 10px;" id="displayTime"></i>

</xsl:template>
</xsl:stylesheet>

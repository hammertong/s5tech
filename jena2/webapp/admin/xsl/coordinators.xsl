<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0"
		xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match="/">

   <table cellspacing="0" cellpadding="5" style="font-size: 10px;">

	<tr class="dataHead">
		<td align="right" class="dataHeadTopLeft" style="width: 30px;">nr.</td>
	   	<td align="right" class="dataHead" style="width: 120px;">coordinator</td>
	   	<td align="right" class="dataHead" style="width: 80px;">canale</td>		
		<td align="right" class="dataHead" style="width: 180px;">stato</td>
		<td align="right" class="dataHead" style="width: 80px;">disconnessioni</td>
		<td align="right" class="dataHead" style="width: 100px;">esls-associate</td>		
		<td align="right" class="dataHeadTopRight" style="width: 180px;">indirizzo di porta</td>
	</tr>

    <xsl:for-each select="coordinators/entry">

	    <tr>

		  <td class="dataLeft" valign="top" align="right">
	      	<b><xsl:value-of select="position()"/></b>
	      </td>

	      <td class="data" align="right">
			<xsl:choose>
				<xsl:when test="mac-address = 'null'">
					non pervenuto
				</xsl:when>
				<xsl:otherwise>	      			
					<xsl:value-of select="mac-address" />
				</xsl:otherwise>
			</xsl:choose>				      				
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
					online da <xsl:value-of select="last-online-time" />
				</td>
			</xsl:when>
			<xsl:otherwise>	
				<td class="data" colspan="2" align="right">
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
		 
		  <td class="data" align="right">
			<xsl:value-of select="offline-count" />	      				
		  </td>	   		  

	      <td class="data" align="right">
			  <xsl:choose>
				<xsl:when test="online = 'true'">
					<xsl:choose>
						<xsl:when test="esls = '0'">
							Nessuna
						</xsl:when>
						<xsl:otherwise>	      			
							<xsl:value-of select="esls" />
						</xsl:otherwise>
					</xsl:choose>			
				</xsl:when>
			  </xsl:choose>
		  </td>	
		  
	      <td class="dataRight" align="right"><xsl:value-of select="port-url" /></td>	

	    </tr>

    </xsl:for-each>

	<tr>
		<td class="dataHeadBottomLeft" colspan="5" align="left">
			Totale: <b style="color: darkBlue;"><xsl:value-of select="count(coordinators//entry)" /></b> 
			accentratori
		</td>
	   	<td class="dataHead" align="right">
			Totale: <b style="color: darkBlue;"><xsl:value-of select="sum(coordinators//entry//esls)" /></b> esls			
		</td>
		<td class="dataHeadBottomRight" align="right" style="color: white;">-</td>
	</tr>

  </table>

<br/>

<i style="font-size: 10px;" id="displayTime"></i>

</xsl:template>
</xsl:stylesheet>

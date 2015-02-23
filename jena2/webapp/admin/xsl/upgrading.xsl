<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0"
		xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match="/">

   <table cellspacing="0" cellpadding="5"  style="font-size: 10px;">

	<tr style="height: 30px;">
		<td align="right" class="dataHeadTopLeft" style="width: 30px;">nr.</td>
		<td align="right" class="dataHeadTopLeft" style="width: 30px;">canale</td>
		<td align="right" class="dataHead" style="width: 150px;">stato-aggiornamento</td>
	   	<td align="right" class="dataHead" style="width: 180px;">avvio</td>
		<td align="right" class="dataHead" style="width: 180px;">termine</td>
		<td align="right" class="dataHead" style="width: 120px;">versione FW</td>
		<td align="right" class="dataHead" style="width: 120px;">tipo ESL</td>
		<td align="right" class="dataHead" style="width: 50px;">t-num</td>
		<td align="right" class="dataHead" style="width: 50px;">t-tot</td>
		<td align="right" class="dataHead" style="width: 50px;">p-num</td>
		<td align="right" class="dataHeadTopRight" style="width: 50px;">p-tot</td>
	</tr>

    <xsl:for-each select="upgrading/entry">

	    <tr style="height: 30px;">

		  <td class="dataLeft" align="right">
			<b><xsl:value-of select="position()"/></b>
	      </td>
		  
		  <td class="dataLeft" align="right">
			<b><xsl:value-of select="channel"/></b>
	      </td>

		  <td class="data" align="right">
			<b style="display: block"><xsl:value-of select="status" /></b>	
			<!-- barra di avanzamento firmware ugrade -->
			<table cellspacing="0" style="heigth: 20px; display: none; width: 250px; border: 0px; color: #1b91c1;">
				<tr>
					<td style="width: 90px; background: #1b91c1; border: 1px 1b91c1 solid;"></td>
					<td style="width: 110px; border: 1px 1b91c1 solid;"></td>
					<td style="color: black; width: 50px; font-size: 12px; border 0px;" align="right">n/a</td>
				</tr>
			</table>
		  </td>	   
		  
		  <xsl:choose>
			<xsl:when test="status = 'upgrading'">
				<td class="data" align="right">
					<xsl:value-of select="last-optime" />	      				
				</td>
				<td class="data" align="right">
					N/A			
				</td>	
			</xsl:when>
			<xsl:otherwise>
				<td class="data">					
				</td>
				<td class="data" align="right">
					<xsl:value-of select="last-optime" />	      				
				</td>
			</xsl:otherwise>
		  </xsl:choose>
		  
	      <td class="data" align="right">
			<xsl:value-of select="fw-version" />	      				
		  </td>
	      
	      <td class="data" align="right">
	      	<xsl:choose>
	      		<xsl:when test="device-type = '0x05'">
					ESL50 (Medium)
	      		</xsl:when>
	      		<xsl:otherwise>
	      			<xsl:choose>
			      		<xsl:when test="device-type = '0x06'">
							ESL70 (Large)
			      		</xsl:when>
			      		<xsl:otherwise>
			      			device-ID <xsl:value-of select="device-type" />
						</xsl:otherwise>
		      		</xsl:choose>
	      		</xsl:otherwise>
      		</xsl:choose>
		  </td>	       
	      
	      <td class="data" align="right" style="width: 30px;">
			<xsl:value-of select="transmission-number" />				      				
		  </td>
	      
	      <td class="data" align="right" style="width: 30px;">
			<xsl:value-of select="total-transmissions" />				      				
		  </td>
	      
	      <td class="data" align="right" style="width: 30px;">
			<xsl:value-of select="packet-number" />				      				
		  </td>
	      
	      <td class="dataRight" align="right" style="width: 30px;">
			<xsl:value-of select="total-packets" />				      				
		  </td>
	      
	    </tr>

    </xsl:for-each>

	<tr style="height: 30px;">
		<td align="left" class="dataHeadBottomLeft"></td>			
		<td colspan="8" align="left" class="dataHead">
			Totale: <b><xsl:value-of select="count(upgrading//entry)" /></b> 
			accentratori con firmware in aggiornamento o aggiornati
		</td>
	   	<td align="left" class="dataHead"></td>
	    <td align="right" class="dataHeadBottomRight" style="color: white;">-</td>
	</tr>

  </table>

<br/>

<i style="padding-left: 15px; font-size: 10px;" id="displayTime"></i>

</xsl:template>
</xsl:stylesheet>

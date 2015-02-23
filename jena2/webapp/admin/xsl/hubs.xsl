<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0"
		xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match="/">

   <table cellspacing="0" cellpadding="5" style="font-size: 10px;">

	<tr class="dataHead">
		<td align="right" class="dataHeadTopLeft" style="width: 30px;">Nr.</td>
	   	<td align="right" class="dataHead" style="width: 120px;">Hub mac address</td>
	   	<td align="right" class="dataHead" style="width: 150px;">Porte Accentratori</td>
		<td align="right" class="dataHead" style="width: 180px;">Comunicazione</td>
		<td align="right" class="dataHeadTopRight" style="width: 180px;">IP Address</td>
	</tr>

    <xsl:for-each select="hubs/entry">

	    <tr>

		  <td class="dataLeft" valign="top" align="right">
	      	<b><xsl:value-of select="position()"/></b>
	      </td>

	      <td class="data" align="right">
			<xsl:choose>
	      		<xsl:when test="version = '1.2'">
					N/P (versione Fw vecchia)
	      		</xsl:when>
	      		<xsl:otherwise>	      			
					<xsl:value-of select="mac-address" />
	      		</xsl:otherwise>
      		</xsl:choose>			
		  </td>	       

	      <td class="data" align="right">
			<xsl:choose>
	      		<xsl:when test="ports = 'n/a'">
					Nessuna porta in ascolto!
	      		</xsl:when>
	      		<xsl:otherwise>	      			
					<xsl:value-of select="ports" />
	      		</xsl:otherwise>
      		</xsl:choose>			
		  </td>	  

		  <td class="data" align="right">
			<xsl:choose>
	      		<xsl:when test="socket = 'ssl'">
					<b style="color: #008000;">SSL (Secure socket)</b>
	      		</xsl:when>
	      		<xsl:otherwise>
					<b style="color: #808080;">TCP Plain socket</b>
	      		</xsl:otherwise>
      		</xsl:choose>
		  </td>

	      <td class="dataRight" align="right"><xsl:value-of select="ip-address" /></td>	

	    </tr>

    </xsl:for-each>

	<tr>
		<td class="dataHeadBottomLeft" colspan="3" align="left">
			Totale: <b style="color: darkBlue;"><xsl:value-of select="count(hubs//entry)" /></b> 
			Hub/s
		</td>
	   	<td class="dataHead" align="left"><font color="white">-</font></td>
		<td class="dataHeadBottomRight" align="right" style="color: white;">-</td>
	</tr>

  </table>

<br/>

<i style="font-size: 10px;" id="displayTime"></i>

</xsl:template>
</xsl:stylesheet>

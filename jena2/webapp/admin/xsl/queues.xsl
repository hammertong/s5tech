<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0"
		xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match="/">

   <table cellspacing="0" cellpadding="5" style="font-size: 10px;">

	<tr class="dataHead">
		<td align="right" class="dataHeadTopLeft" style="width: 30px;">nr.</td>
	   	<td align="right" class="dataHead" style="width: 120px;">coordinator</td>
	   	<td align="right" class="dataHead" style="width: 80px;">canale</td>		
		<td align="right" class="dataHead" style="width: 180px;">price-updates</td>
		<td align="right" class="dataHead" style="width: 80px;">altri-comandi</td>
		<td align="right" class="dataHead" style="width: 100px;">broadcast</td>
		<td align="right" class="dataHead" style="width: 100px;">status</td>
		<td align="right" class="dataHead" style="width: 100px;">in uscita</td>
		<td align="right" class="dataHeadTopRight" style="width: 180px;">indirizzo di porta</td>
	</tr>

    <xsl:for-each select="queues/entry">

	    <tr>

		  <td class="dataLeft" valign="top" align="right">
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
			</xsl:when>
			<xsl:otherwise>
			  <td class="data" align="right">
				offline
			  </td>	 
			</xsl:otherwise>
		  </xsl:choose>
		  
	      <td class="data" align="right">
			<xsl:choose>
	      		<xsl:when test="esl = '0'">
					Nessuno
	      		</xsl:when>
	      		<xsl:otherwise>	   
					<b style="color: #DF0000;">				
						<xsl:value-of select="esl" /> in attesa
					</b>
	      		</xsl:otherwise>
      		</xsl:choose>			
		  </td>	

		  <td class="data" align="right">
			<xsl:choose>
	      		<xsl:when test="esl-low = '0'">
					Nessuno
	      		</xsl:when>
	      		<xsl:otherwise>	      			
					<b style="color: #DF0000;">
						<xsl:value-of select="esl-low" /> in attesa
					</b>
	      		</xsl:otherwise>
      		</xsl:choose>			
		  </td>	
		  
		  <td class="data" align="right">
			<xsl:choose>
	      		<xsl:when test="bcast = '0'">
					Nessuno
	      		</xsl:when>
	      		<xsl:otherwise>
					<b style="color: #DF0000;">
						<xsl:value-of select="bcast" /> in attesa
					</b>
	      		</xsl:otherwise>
      		</xsl:choose>			
		  </td>	
		  
		  <td class="data" align="right">
			<xsl:choose>
	      		<xsl:when test="status = '0'">
					Nessuna
	      		</xsl:when>
	      		<xsl:otherwise>	
					<b style="color: #DF0000;">
						<xsl:value-of select="status" /> in attesa
					</b>
	      		</xsl:otherwise>
      		</xsl:choose>			
		  </td>	
		  
		  <td class="data" align="right">
			<xsl:choose>
	      		<xsl:when test="outgoing = '0'">
					Nessuno
	      		</xsl:when>
	      		<xsl:otherwise>	      			
					<b style="color: #DF0000;">
						<xsl:value-of select="outgoing" />
					</b>
	      		</xsl:otherwise>
      		</xsl:choose>			
		  </td>	
		  
	      <td class="dataRight" align="right"><xsl:value-of select="port-url" /></td>	

	    </tr>

    </xsl:for-each>

	<tr>
		<td class="dataHeadBottomLeft" colspan="8" align="left">
			Totale: <b style="color: darkBlue;"><xsl:value-of select="count(queues//entry)" /></b> 
			accentratori
		</td>
		<td class="dataHeadBottomRight" align="right" style="color: white;">-</td>
	</tr>

  </table>

<br/>

<i style="font-size: 10px;" id="displayTime"></i>

</xsl:template>
</xsl:stylesheet>

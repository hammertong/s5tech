<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0"
		xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match="/">

   <table cellspacing="0" cellpadding="5"  style="font-size: 10px;">

	<tr style="height: 30px;">
		<td class="dataHeadTopLeft" align="right" style="width: 30px;">Nr.</td>
	   	<td class="dataHead" align="right" style="width: 180px;">Nome task</td>
	   	<td class="dataHead" align="right" style="width: 120px;">CPU %</td>
		<td class="dataHead" align="center" style="width: 120px;">Stato</td>
		<td class="dataHead" align="center" style="width: 80px;">Deadlocked</td>
		<td class="dataHeadTopRight" align="center" style="width: 100px;">Thread id</td>		
	</tr>

    <xsl:for-each select="threads/entry">

	    <tr>

		  <td class="dataLeft" valign="top" align="right">
	      	<b><xsl:value-of select="position()"/></b>
	      </td>

	      <td class="data" align="left"><xsl:value-of select="name" /></td>	

	      <td class="data" align="right"><xsl:value-of select="cpu" /></td>	

	      <td class="data" align="center"><xsl:value-of select="state" /></td>	

		  <td class="data" align="center">
			<xsl:choose>
	      		<xsl:when test="deadlocked = 'true'">
					<b style="color: red;">yes</b>
	      		</xsl:when>
	      		<xsl:otherwise>
					<i style="color: green;">no</i>
	      		</xsl:otherwise>
      		</xsl:choose>			
		  </td>	  
		  
		  <td class="dataRight" align="center"><xsl:value-of select="id" /></td>
		  
	    </tr>

    </xsl:for-each>

	<tr style="height: 30px;">
		<td align="left" class="dataHeadBottomLeft" style="color: white;">-</td>			
		<td colspan="4" align="left" class="dataHead">
			Totale: <b style="color: darkBlue"><xsl:value-of select="count(threads//entry)" /></b> 
			tasks
		</td>
	    <td align="right" class="dataHeadBottomRight" style="color: white;">-</td>
	</tr>

  </table>

<br/>
<i style="padding-left: 15px; font-size: 10px;" id="displayTime"></i>

</xsl:template>
</xsl:stylesheet>

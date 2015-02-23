<%@ page contentType="text/xml" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match="/">

<xsl:choose>
<xsl:when test="count(list//Alerts) > 0">

	<table cellspacing="0" cellpadding="0" class="square" style="width: 100%; font-size: 12px;">
   
		<tr class="header" style="font-weight: bolder; height: 32px;">		
			<td style="width: 30px;"><img style="width: 32px; height: 32px; opacity: 0.0;" src="images/expand.png"></img></td>
			<td align="center" style="width: 30px;"><bean:message key="xslt.nr"/></td>
			<td>
				<bean:message key="xslt.alerts.alertType"/>				
			</td>
			<td><bean:message key="xslt.alerts.title"/></td>
			<td><bean:message key="xslt.alerts.lastUpdate"/></td>
		</tr>
		
		<xsl:for-each select="list/Alerts">	
	    <tr valign="middle">			
			<xsl:attribute name="class">
				<xsl:choose>
					<xsl:when test="(position() mod 2) != 1">
						<xsl:text>even</xsl:text>
					</xsl:when>
					<xsl:otherwise>
						<xsl:text>odd</xsl:text>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>			
			<td class="header" onclick="showmesg(this)" style="cursor: pointer; width: 50px;">				
				<xsl:attribute name="id">
					<xsl:value-of select="id" />			
				</xsl:attribute>			
				<img class="trans" style="width: 32px; height: 32px;" src="images/expand.png"></img>
			</td>
			<td align="center" style="width: 30px: font-weight: bolder;">
				<xsl:value-of select="position()"/>				
			</td>
			<td>
				<xsl:value-of select="alertType" />			
			</td>			  
			<td>
				<xsl:value-of select="title" />			
			</td>			  			
			<td>
				<xsl:value-of select="lastUpdate" />
			</td>				
	    </tr>
		<tr>
			<xsl:attribute name="class">
				<xsl:choose>
					<xsl:when test="(position() mod 2) != 1">
						<xsl:text>even</xsl:text>
					</xsl:when>
					<xsl:otherwise>
						<xsl:text>odd</xsl:text>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<td class="header"></td>
			<td></td>
			<td colspan="3" style="padding-bottom: 15px;">
				<div style="display: none;">
					<xsl:attribute name="id">
						<xsl:value-of select="concat('div', id)" />
					</xsl:attribute>
					!load
				</div>
			</td>
		</tr>
		</xsl:for-each>
	
		<tr class="header" style="font-weight: bolder; height: 32px;">
			<td style="width: 30px;"><img style="width: 32px; height: 32px; opacity: 0.0;" src="images/expand.png"></img></td>
			<td colspan="4">
				<bean:message key="xslt.total"/> <b><xsl:value-of select="count(list//Alerts)" /></b> 			
			</td>	   	
		</tr>
	
	</table>  
		
</xsl:when>
<xsl:otherwise>	

	<b><bean:message key="xslt.alerts.noupdates"/></b>
	
</xsl:otherwise>
</xsl:choose>
  
</xsl:template>
</xsl:stylesheet>

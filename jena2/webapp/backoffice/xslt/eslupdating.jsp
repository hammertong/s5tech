<%@ page contentType="text/xml" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match="/">

<xsl:choose>
<xsl:when test="count(list//EslUpdates) > 0">

	<table cellspacing="0" cellpadding="5" class="square" width="100%">	
		
		<tr class="header">
			<td><bean:message key="xslt.nr"/></td>
			<td><bean:message key="xslt.eslupdating.mac"/></td>
			<td colspan="2"><bean:message key="xslt.eslupdating.status"/></td>
			<td><bean:message key="xslt.eslupdating.pubtime"/></td>
			<td><bean:message key="xslt.eslupdating.sentprice"/></td>		
			<td><bean:message key="xslt.eslupdating.sentppu"/></td>
			<td><bean:message key="xslt.eslupdating.code"/></td>		
			<td><bean:message key="xslt.eslupdating.desc"/></td>
		</tr>
		
		<xsl:for-each select="list/EslUpdates">	
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
			<td>
				<b>
					<xsl:value-of select="position()"/>
				</b>
			</td>
			<td>
				<xsl:value-of select="esl" />			
			</td>			  
			<td>
				<xsl:choose>
					<xsl:when test="acknoledgeDate != ''">
						<img src="images/led-green.png" class="led"></img>						
					</xsl:when>
					<xsl:otherwise>	  
						<img src="images/led-red.png" class="led"></img>
					</xsl:otherwise>
				</xsl:choose>				
			</td>				
			<td>
				<xsl:choose>
					<xsl:when test="acknoledgeDate != ''">
						<bean:message key="xslt.eslupdating.received"/>
					</xsl:when>
					<xsl:otherwise>	  
						<bean:message key="xslt.eslupdating.waiting"/>
					</xsl:otherwise>
				</xsl:choose>								
			</td>	
			<td>
				<input type="text" style="background: transparent; border: 0px; width: 100%; font-size: 10px;">
					<xsl:attribute name="value">
						<xsl:value-of select="publishingDate" />			
					</xsl:attribute>
				</input>
			</td>			  
			<td>
				<xsl:value-of select="sentPrice" />			
			</td>	
			<td>
				<xsl:value-of select="sentPricePerUnit" />			
			</td>	
			<td>
				<xsl:value-of select="product" />			
			</td>	
			<td>
				<input type="text" style="background: transparent; border: 0px; width: 100%; font-size: 10px;">
					<xsl:attribute name="value">
						<xsl:value-of select="productDescription" />	
					</xsl:attribute>
				</input>				
			</td>	
	    </tr>
		</xsl:for-each>
		
		<tr class="header">
			<td colspan="9">
				<bean:message key="xslt.total"/> <b><xsl:value-of select="count(list//EslUpdates)" /></b> 			
			</td>
		</tr>
		
	</table>
  	
</xsl:when>
<xsl:otherwise>		
	<b><bean:message key="xslt.eslupdating.noupdates"/></b>
</xsl:otherwise>
</xsl:choose>
  
</xsl:template>
</xsl:stylesheet>

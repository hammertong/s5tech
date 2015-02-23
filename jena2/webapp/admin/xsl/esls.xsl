<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0"
		xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match="/">

	<script type="text/javascript">
	<![CDATA[
		
		
		]]>
	</script>
  
	 <table cellspacing="0" cellpadding="5" class="rounded">
			
		<tr class="header">			
			<td style="border-bottom: 1px solid;" colspan="2">Nr</td>
			<td style="border-left: 1px solid; border-bottom: 1px solid;" colspan="2">ESL</td>			
			<td style="border-left: 1px solid; border-bottom: 1px solid;" colspan="3">IEEE 802.15.4</td>			
			<td style="border-left: 1px solid; border-bottom: 1px solid;" colspan="5">Commands</td>			
		</tr>	
			
		<tr class="header">			
			<td style="border-left: 1px solid; border-bottom: 1px solid;"></td>
			<td style="border-left: 1px solid; border-bottom: 1px solid;"></td>
			<td style="border-left: 1px solid; border-bottom: 1px solid;">Mac Address</td>
			<td style="border-left: 1px solid; border-bottom: 1px solid;">Type</td>
			<td style="border-left: 1px solid; border-bottom: 1px solid;">Id</td>
			<td style="border-left: 1px solid; border-bottom: 1px solid;">Channel</td>
			<td style="border-left: 1px solid; border-bottom: 1px solid;">Coordinator</td>
			<td align="center" style="border-left: 1px solid; border-bottom: 1px solid;">leave</td>
			<td align="center" style="border-left: 1px solid; border-bottom: 1px solid;">kill</td>			
			<td align="center" style="border-left: 1px solid; border-bottom: 1px solid;">stats</td>			
			<td align="center" style="border-left: 1px solid; border-bottom: 1px solid;" colspan="2">join</td>						
		</tr>
			
		<xsl:for-each select="esls/entry">
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
				<td align="right">
					<xsl:value-of select="position()" />
				</td>
				<td align="center">				
					<xsl:choose>
						<xsl:when test="channel != ''">
							<img class="led" src="images/led-green.png"></img>
						</xsl:when>
						<xsl:otherwise>
							<img class="led" src="images/led-red.png"></img>
						</xsl:otherwise>
					</xsl:choose>
				</td>
				<td>
					<span style="font-family: Lucida Console, Courier New, Courier;">
					<xsl:value-of select="mac" />
					</span> 
				</td>
				<td align="right">
					<xsl:value-of select="eslType" />
				</td>
				<td align="right">
					<span style="font-family: Lucida Console, Courier New, Courier;">
					<xsl:choose>
						<xsl:when test="shortAddress = ''">
							------
						</xsl:when>
						<xsl:otherwise>
							0x<xsl:value-of select="shortAddress" />
						</xsl:otherwise>
					</xsl:choose>      		
					</span>
				</td>
				<td align="right">
					<xsl:value-of select="channel" />
				</td>
				<td>
					<span style="font-family: Lucida Console, Courier New, Courier;">
					<xsl:choose>
						<xsl:when test="coordinatorMac = 'null'">
							------
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="coordinatorMac" />
						</xsl:otherwise>
					</xsl:choose>      		
					</span>					
				</td>
				<td>					
					<xsl:choose>
						<xsl:when test="channel != ''">
							<img class="trans" style="cursor: pointer;" src="images/leave.png" onclick="doAction(this)">
								<xsl:attribute name="id">
									leave_<xsl:value-of select="mac" />
								</xsl:attribute>
							</img>
						</xsl:when>
						<xsl:otherwise>
							<img class="disabled" src="images/leave.png"></img>
						</xsl:otherwise>
					</xsl:choose>					
				</td>
				<td>	
					<xsl:choose>
						<xsl:when test="channel != ''">
							<img class="trans" style="cursor: pointer;" src="images/kill.png" onclick="doAction(this)">
								<xsl:attribute name="id">
									kill_<xsl:value-of select="mac" />
								</xsl:attribute>
							</img>
						</xsl:when>
						<xsl:otherwise>
							<img class="disabled" src="images/kill.png"></img>
						</xsl:otherwise>
					</xsl:choose>					
				</td>
				<td>
					<xsl:choose>
						<xsl:when test="channel != ''">
							<img class="trans" style="cursor: pointer;" src="images/status.png" onclick="doAction(this)">
								<xsl:attribute name="id">
									stats_<xsl:value-of select="mac" />
								</xsl:attribute>
							</img>
						</xsl:when>
						<xsl:otherwise>
							<img class="disabled" src="images/status.png"></img>
						</xsl:otherwise>
					</xsl:choose>
				</td>
				<td>
					<xsl:choose>
						<xsl:when test="channel != ''">
							<img class="trans" style="cursor: pointer;" src="images/channel.png" onclick="doAction(this)">
								<xsl:attribute name="id">
									join_<xsl:value-of select="mac" />
								</xsl:attribute>
							</img>
						</xsl:when>
						<xsl:otherwise>
							<img class="disabled" src="images/channel.png"></img>
						</xsl:otherwise>
					</xsl:choose>
				</td>
				<td>			
					<xsl:choose>
						<xsl:when test="channel != ''">
							<select>						
								<xsl:attribute name="id">selchannel_<xsl:value-of select="mac" /></xsl:attribute>																
								<xsl:choose>
									<xsl:when test="channel != '12'">
										<option value="12">12</option>
									</xsl:when>
								</xsl:choose>
								<xsl:choose>
									<xsl:when test="channel != '13'">
										<option value="13">13</option>
									</xsl:when>
								</xsl:choose>
								<xsl:choose>
									<xsl:when test="channel != '14'">
										<option value="14">14</option>
									</xsl:when>
								</xsl:choose>
								<xsl:choose>
									<xsl:when test="channel != '15'">
										<option value="15">15</option>
									</xsl:when>
								</xsl:choose>
								<xsl:choose>
									<xsl:when test="channel != '16'">
										<option value="16">16</option>
									</xsl:when>
								</xsl:choose>
								<xsl:choose>
									<xsl:when test="channel != '17'">
										<option value="17">17</option>
									</xsl:when>
								</xsl:choose>
								<xsl:choose>
									<xsl:when test="channel != '18'">
										<option value="18">18</option>
									</xsl:when>
								</xsl:choose>
								<xsl:choose>
									<xsl:when test="channel != '19'">
										<option value="19">19</option>
									</xsl:when>
								</xsl:choose>
								<xsl:choose>
									<xsl:when test="channel != '20'">
										<option value="20">20</option>
									</xsl:when>
								</xsl:choose>
								<xsl:choose>
									<xsl:when test="channel != '21'">
										<option value="21">21</option>
									</xsl:when>
								</xsl:choose>
								<xsl:choose>
									<xsl:when test="channel != '22'">
										<option value="22">22</option>
									</xsl:when>
								</xsl:choose>
								<xsl:choose>
									<xsl:when test="channel != '23'">
										<option value="23">23</option>
									</xsl:when>
								</xsl:choose>
								<xsl:choose>
									<xsl:when test="channel != '24'">
										<option value="24">24</option>
									</xsl:when>
								</xsl:choose>
								<xsl:choose>
									<xsl:when test="channel != '25'">
										<option value="25">25</option>
									</xsl:when>
								</xsl:choose>
								<xsl:choose>
									<xsl:when test="channel != '26'">
										<option value="26">26</option>
									</xsl:when>
								</xsl:choose>								
							</select> 
						</xsl:when>
						<xsl:otherwise>
							----
						</xsl:otherwise>
					</xsl:choose>
				</td>				
			</tr>   
		</xsl:for-each>
		      
		<tr class="header">
			<td colspan="12">
				Totale:
				<b id="count"><xsl:value-of select="count(esls//entry)" /></b> 				
			</td>
		</tr>

	</table>

</xsl:template>
</xsl:stylesheet>

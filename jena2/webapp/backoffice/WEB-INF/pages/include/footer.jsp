<%@ page import="com.s5tech.backend.IConstants" %>
<!-- begin footer section -->	
<logic:present name="<%= IConstants.USER_VIEW_KEY %>">	
		</td>
	</tr>	
	<tr valign="top">			
		<td style="padding-top: 0px; padding-left: 20px; padding-right: 20px; padding-bottom: 20px;" id="bottomview">			
			&nbsp;
		</td>
	</tr>	
	<tr valign="middle">			
		<td colspan="2" class="statusbar">				
			<table cellspacing="0" cellpadding="0" border="0" width="100%">
				<tr>
					<td id="statusbar">						
						<logic:notEmpty name="statusbar">
							<bean:write name="statusbar" />
						</logic:notEmpty>
						<div style="display: none;" id="statusbar.lastupdate">
							<bean:message key="pricer.lastupdate" />			
						</div>
					</td>					
					<td id="version" style="width: 200px;" align="right">	
						Running Network Version 1.6.00
					</td>
					<td id="version.led" style="width: 20px;" align="center">	
						<img src="images/led-green.png" class="trans led"></img>						
					</td>					
					<td id="backend" style="width: 100px;" align="right">						
						Backend runnning
					</td>
					<td id="backend.led" style="width: 20px;" align="center">	
						<img src="images/led-green.png" class="trans led"></img>						
					</td>
				</tr>
				
			</table>
		</td>
	</tr>	
</table>
</logic:present>

<br>
<br>

<div id="footer" align="center">
	<div class="copyright">
		<logic:present name="<%= IConstants.USER_VIEW_KEY %>">
			<bean:message key="footer.customeragreement"/>	
			<br>
		</logic:present>				
		<bean:message key="footer.copyrigth"/>	
		<br>
	</div>
	<br>
	<div>
		<a href="https://twitter.com/S5Tech" target="_blank" style="color: white;">
			<html:img srcKey="image.twitter.logo" altKey="image.twitter.logo.alt" styleClass="trans ico"/>
		</a>
		<a href="https://www.linkedin.com/company/900485" target="_blank" style="color: white;">
			<html:img srcKey="image.linkedin.logo" altKey="image.linkedin.logo.alt" styleClass="trans ico"/>
		</a>
		<a href="https://angel.co/s5tech" target="_blank" style="color: white;">
			<html:img srcKey="image.angels.logo" altKey="image.angels.logo.alt" styleClass="trans ico"/>
		</a>		
	</div>
</div>

</center>

<%-- focus on error field if specified in request.setAttribute("focus", fieldname) --%>

<logic:present name="focus">
<script type="text/javascript">
<!--
for (var i = 0; i < document.forms.length; i ++) {
	if (document.forms[i].<bean:write name="focus" />) {		
		var field = document.forms[i].<bean:write name="focus" />;
		field.style.background = '#ffcccc';
		field.style.color = '#ee0000';
		if (field.type == 'text') {
			field.setSelectionRange(0, field.value.length);
		}
		field.focus();					
		break;
	}
}
// -->
</script>
</logic:present>

<logic:present name="view3d">
</div>
<script type="text/javascript">
	
	// Create an instance of Meny
	var meny = Meny.create({
		// The element that will be animated in from off screen
		menuElement: document.querySelector( '.meny' ),

		// The contents that gets pushed aside while Meny is active
		contentsElement: document.querySelector( '.contents' ),

		// [optional] The alignment of the menu (top/right/bottom/left)
		position: Meny.getQuery().p || 'left',

		// [optional] The height of the menu (when using top/bottom position)
		height: 200,

		// [optional] The width of the menu (when using left/right position)
		width: 260,

		// [optional] Distance from mouse (in pixels) when menu should open
		threshold: 40,

		// [optional] Use mouse movement to automatically open/close
		mouse: true,

		// [optional] Use touch swipe events to open/close
		touch: true
	});

	// API Methods:
	// meny.open();
	// meny.close();
	// meny.isOpen();

	// Events:
	// meny.addEventListener( 'open', function(){ console.log( 'open' ); } );
	// meny.addEventListener( 'close', function(){ console.log( 'close' ); } );

	// Embed an iframe if a URL is passed in
	if( Meny.getQuery().u && Meny.getQuery().u.match( /^http/gi ) ) {
		var contents = document.querySelector( '.contents' );
		contents.style.padding = '0px';
		contents.innerHTML = '<div class="cover"></div><iframe src="'+ Meny.getQuery().u +'" style="width: 100%; height: 100%; border: 0; position: absolute;"></iframe>';
	}
	
</script>
</logic:present>

<!--end footer section -->

/**********************************************************************************
 *
 *           S5TECH(c) NETWORK APPLICATION DOCUMENTATION AND LICENSE
 *                        Version 1.6, September 2014
 *                          http://www.s5tech.com/
 *
 * Permission to copy, modify, and distribute this software and its documentation,
 * with or without modification, for any purpose and without fee or royalty is
 * hereby granted.
 * 
 * THIS SOFTWARE AND DOCUMENTATION IS PROVIDED "AS IS," AND COPYRIGHT HOLDERS MAKE
 * NO REPRESENTATIONS OR WARRANTIES, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO, WARRANTIES OF MERCHANTABILITY OR FITNESS FOR ANY PARTICULAR PURPOSE OR THAT
 * THE USE OF THE SOFTWARE OR DOCUMENTATION WILL NOT INFRINGE ANY THIRD PARTY
 * PATENTS, COPYRIGHTS, TRADEMARKS OR OTHER RIGHTS.
 * 
 * COPYRIGHT HOLDERS WILL NOT BE LIABLE FOR ANY DIRECT, INDIRECT, SPECIAL OR
 * CONSEQUENTIAL DAMAGES ARISING OUT OF ANY USE OF THE SOFTWARE OR DOCUMENTATION.
 * 
 * The name and trademarks of  copyright holders may NOT be used in advertising or
 * publicity pertaining to the software without specific, written prior permission.
 * Title to copyright in this software and any associated documentation will at
 * all times remain with copyright holders.
 * 
 * FOR INFORMATION ABOUT OBTAINING, INSTALLING AND RUNNING THIS SOFTWARE WRITE AN
 * EMAIL TO assist@s5tech.com
 * 
 * S5Tech Development Team 2015-01-15
 * S5Tech S.P.A, Via Caboto 10, 20100 Legnano - Italy
 * 		
 *********************************************************************************/
 
package com.s5tech.net.services.display;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import com.itextpdf.text.pdf.BarcodeEAN;

public class GeneratePngDisplay {
	
	static final int DEFAULT_WIDTH = 200;	
	static final int DEFAULT_HEIGHT = 96;
	
	private void pipeStream(InputStream input, OutputStream output)
			   throws IOException
	{
	   byte buffer[] = new byte[1024];
	   int numRead = 0;

	   do
	   {
	      numRead = input.read(buffer);
	      output.write(buffer, 0, numRead);
	   } while (input.available() > 0);

	   output.flush();
	}
	
	private Image getImageResized(Image img, String factorList)
	{
		if (factorList == null || factorList.length() == 0 || factorList.equals("100%")) return img;
		
		int h = img.getHeight(null);
		int w = img.getWidth(null);
	
		for (String factor : factorList.split(" "))
		{
			factor = factor.trim();
			
			if (factor.length() == 0) continue;
			
			if (factor.equals("100%")) continue;
			
			if (factor.endsWith("%"))
	        {
	        	float fx = Float.parseFloat(factor.substring(0, factor.length() - 1));
	        	if (fx <= 0 || fx >= 100) {
	        		System.err.println("ignored resize image factor: " + ((int)fx) + "% ...!");
	        	}
	        	else {
	        		System.out.println("resizing image scale to " + ((int) fx) + "%");
	        		fx /= 100;	
	        		img = img.getScaledInstance(((int)(w * fx)), -1, Image.SCALE_SMOOTH);
	        	}
	        }
	        else if (factor.toLowerCase().endsWith("/h")) 				        
	        {
	        	int new_h = Integer.parseInt(factor.substring(0, factor.length() - 2));
	        	if (new_h <= 0 || new_h > h) {
	        		System.err.println("ignored resize image height: " + new_h + "px ...!");
	        	}
	        	else {
	        		System.out.println("resizing image height " + new_h + "px");
	        		img = img.getScaledInstance(-1, new_h, Image.SCALE_SMOOTH);
	        	}
	        }
	        else if (factor.toLowerCase().endsWith("/w")) 				        
	        {
	        	int new_w = Integer.parseInt(factor.substring(0, factor.length() - 2));
	        	if (new_w <= 0 || new_w > w) {
	        		System.err.println("ignored resize image width: " + new_w + "px ...!");
	        	}
	        	else {
	        		System.out.println("resizing image width " + new_w + "px");
	        		img = img.getScaledInstance(new_w, -1, Image.SCALE_SMOOTH);
	        	}
	        }
	        else if (factor.toLowerCase().endsWith("/x")) 				        
	        {
	        	int crop_x = Integer.parseInt(factor.substring(0, factor.length() - 2));
	        	if (crop_x <= 0 || crop_x > w) {
	        		System.err.println("ignored crop image width: " + crop_x + "px ...!");
	        	}
	        	else {
	        		System.out.println("cropping image height " + crop_x + "px");
	        		BufferedImage dest = new BufferedImage(crop_x, h, BufferedImage.TYPE_INT_ARGB);  
	        		Graphics2D g2 = dest.createGraphics();  
	        		g2.drawImage(img, 0, 0, null);  
	        		g2.dispose(); 
	        		img = dest;
	        	}
	        }
	        else if (factor.toLowerCase().endsWith("/y")) 				        
	        {
	        	int crop_y = Integer.parseInt(factor.substring(0, factor.length() - 2));
	        	if (crop_y <= 0 || crop_y > h) {
	        		System.err.println("ignored crop image height: " + crop_y + "px ...!");
	        	}
	        	else {
	        		System.out.println("cropping image height " + crop_y + "px");
	        		BufferedImage dest = new BufferedImage(w, crop_y, BufferedImage.TYPE_INT_ARGB);  
	        		Graphics2D g2 = dest.createGraphics();  
	        		g2.drawImage(img, 0, 0, null);  
	        		g2.dispose(); 
	        		img = dest;
	        	}
	        }
			
		}
		
		return img;
		
	}
	
	/**
	 * 
	 * Create display image and save it to specified file<br>
	 * with a given list of label definitions<br>
	 * <ol>
	 * <li>default size is 200x96 (if you set x <= 0 and y <= 0)</li>
	 * <li>output format is taken from outputFile's extension (.png =&gt; png, etc...)</li>
	 * </ol>
	 * <pre>
     * <b>labels</b> definition syntax: 
     * 	{ "fontname, fonttype, fontsize, x, y, text" }
     * if fontname = barcode ...
     * 	{ "barcode, type (ean13 ...), barcode-height, x, y, code" }
     * if fontname = image ...
     * 	{ "image, [reserved], [reserved], x, y, &lt;imagefile path&gt;" }
     * </pre>
	 * 
	 * @param labels
	 * @param params
	 * @param outputFile
	 * @param width
	 * @param height
	 * @throws IOException
	 * @throws FontFormatException
	 */
	public void create (
			List<String> labels, 
			HashMap<String, String> params, 
			String outputFile,
			int width, 
			int height) throws IOException, FontFormatException {
		
		int rsscount = 0;
		
		int w = width > 0 ? width : DEFAULT_WIDTH;
		int h = height > 0 ? height : DEFAULT_HEIGHT;
		
        // Create a buffered image with transparency
        System.out.println("generating display " + w + "x" + h + ", destination: " + outputFile + " ...");
		BufferedImage bufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr = bufferedImage.createGraphics();
        
        // Create blank background        
        bGr.setColor(Color.WHITE);
        bGr.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
        
        //
        // starting display creation ...
        //
        
        bGr.setColor(Color.BLACK);
        
        for (String label : labels) {
        	
        	//
    		// parameters substitutions
    		//
    		if (params != null && params.size() > 0) {
	    		while (label.indexOf('$') >= 0) {
	    			int idx = label.indexOf('$');
	    			String left = label.substring(0, idx);
	    			String right = label.substring(idx + 1);
	    			for (String k : params.keySet()) {
	    				String v = params.get(k);
	    				if (right.startsWith(k)) {
	    					right = right.replaceFirst(k, v);
	    				}
	    			}
	    			label = left + right;
	    		}
    		}
        	
        	String [] f = label.split(";");
        	for (int i = 0; i < f.length; i ++) {
        		f[i] = f[i].replace('\t', ' ').trim();
        	}
        	
        	if (f.length < 6) {
        		System.err.println("insufficient fields number, skipped record > " + label);
        		continue;
        	}

        	boolean isNumeric = true;
        	for (int i = 0; i < f[2].length(); i ++) {
        		if ("0123456789".indexOf(f[2].charAt(i)) < 0) {
        			isNumeric = false;
        			break;
        		}
        	}
        	
        	int size = (f[2].length() > 0 && isNumeric ? Integer.parseInt(f[2]) : 12);

    		int x = Integer.parseInt(f[3]);
    		boolean anchor_right = f[3].startsWith("-");
    		int y = Integer.parseInt(f[4]);
    		boolean anchor_bottom = f[4].startsWith("-");

    		String text = f[5];
    		
    		//
    		// special characters substitutions
    		//
    		while (text.indexOf("\\euro") >= 0) {
    			int idx = text.indexOf("\\euro");
    			text = text.substring(0, idx) 
    					+ "\u20ac" 
    					+ text.substring(idx + 5);
    		}

        	if (f[0].trim().equalsIgnoreCase("barcode"))
        	{        	
        		
        		if (f[1].trim().equalsIgnoreCase("ean13"))
        		{
		        	String code = text;
		        	
		        	System.err.println("adding barcode EAN13 > " + code);
					
		        	BarcodeEAN barcode = new BarcodeEAN();		
			       
			        barcode.setCodeType(BarcodeEAN.EAN13);
			        barcode.setGenerateChecksum(true);
			        barcode.setCode(code);
			        barcode.setBarHeight(size);        
			        
			        Image img = barcode.createAwtImage(Color.BLACK, Color.WHITE);
			        
			        if (anchor_right)
	        		{
	        			int n = img.getWidth(null);
	        			x += w - n;
	        		}
	        		
	        		if (anchor_bottom)
	        		{
	        			y += h - img.getHeight(null);
	        		}
			        
			        bGr.drawImage(img, x, y, null);
        		}
        		else 
        		{
        			System.err.println("error: invalid barcode or barcode not supported, skipped record > " + label);
            		continue;
        		}
        		
        	}
        	else if (f[0].trim().equalsIgnoreCase("image"))
        	{ 
        		System.err.println("adding image file > " + text + " (" + x + ", " + y + ")");
        		
        		File file = new File(text); 
        		if (!file.exists() || !file.isFile()) {
        			System.err.println("error: file " + text + " not found - cannot add image!");
        			continue;
        		}
         		
		        Image img = getImageResized(ImageIO.read(file), f[2].trim());
        		         		
        		if (anchor_right)
        		{
        			int n = img.getWidth(null);
        			x += w - n;
        		}
        		
        		if (anchor_bottom)
        		{
        			y += h - img.getHeight(null);
        		}
		        
		        bGr.drawImage(img, x, y, null);
        		
        	}
        	else if (f[0].trim().equalsIgnoreCase("rss"))
        	{ 
        		rsscount ++;
        		
        		String fname = "out" + rsscount + ".bmp";
        		
        		String [] RSS_TYPES_ARRAY = new String [] {
        			"UNDEFINED",
        			"RSS14",
        			"RSS14T", 
        			"RSS14S",
        			"RSS14SO",
        			"RSSL",
        			"RSSE",
        			"UPC-A",
        			"UPC-E",
        			"EAN-13",
        			"EAN-8",
        			"EAN-128-A/B",
        			"EAN-128-C"
        		};
        		
        		String tk [] = f[1].toUpperCase().split(",");
        		for (int l = 0; l < tk.length; l ++) {
        			tk[l] = tk[l].trim();
        		}
                
        		int systype = 0; 
        		for (int l = 1; l < RSS_TYPES_ARRAY.length; l ++) {
	        		if (tk[0].equals(RSS_TYPES_ARRAY[l])) {
	        			System.err.println("adding rss barcode " + RSS_TYPES_ARRAY[l] 
	        					+ " (" + l + ") > " + text + " (" + x + ", " + y + ")");
	        			systype = l;
	        			break;	        			
	        		}
        		}
        		
        		if (systype == 0) {
        			System.err.println("rss barcode not valid! (" + tk[0] + ")");
        			continue;
        		}

        		int ppx = tk.length > 1 ? Integer.parseInt(tk[1]) : 4;
        		int xpu = tk.length > 2 ? Integer.parseInt(tk[2]) : 1;
        		int ypu = tk.length > 3 ? Integer.parseInt(tk[3]) : 2;
        		
        		PrintStream o = new PrintStream("rss.in");
        		o.println(systype);
        		o.println(0);
        		o.println(ppx);        		
        		o.println(1);
        		o.println(xpu);
        		o.println(2);
        		o.println(ypu);
        		o.println(4);
        		o.println(0);
        		o.println(6);
        		o.println(1);
        		o.println(3);
        		o.println(fname);
        		o.println(5);
        		o.println(text);
        		o.println(9);
        		o.println(0);
        		o.close();
        		
        		InputStream fileIn = new FileInputStream("rss.in");
        		Process process = Runtime.getRuntime().exec("./tools/rssenc-vc.exe");
        		OutputStream procOut = process.getOutputStream();

				pipeStream (fileIn, procOut);

        		Image img = getImageResized(ImageIO.read(new File(fname)), f[2].trim());

        		if (anchor_right)
        		{
        			int n = img.getWidth(null);
        			x += w - n;
        		}
        		
        		if (anchor_bottom)
        		{
        			y += h - img.getHeight(null);
        		}
		        
		        bGr.drawImage(img, x, y, null);

        	}
        	else 
        	{
        		String fname = f[0].trim();
        		
        		System.out.println("adding label > " + text);
        		
        		int fsize = 14;
        		int ftype = Font.PLAIN;
        		
        		if (f[1].trim().toLowerCase().charAt(0) == 'b') ftype = Font.BOLD;
        		else if (f[1].trim().toLowerCase().charAt(0) == 'i') ftype = Font.ITALIC;    
        		
        		fsize = f[2].trim().length() > 0 ? Integer.parseInt(f[2]) : 14;
        		
    			if (fname.toLowerCase().endsWith(".ttf")) 
    			{
    				File file = new File(fname);
    				if (file.exists()) {        					
    					Font font = Font.createFont(Font.TRUETYPE_FONT, file);
    					bGr.setFont(font.deriveFont(ftype, fsize));
    				}
    				else {
    					System.err.println("error: cannot load 'true type font', file " + fname + " not found");
    				}
    			}
    			else 
    			{
    				bGr.setFont(new Font(fname, ftype, fsize));
    			}        			
        		
        		if (anchor_right) {
        			int n = bGr.getFontMetrics().stringWidth(text);
        			x += w - n;
        		}
        		
        		if (anchor_bottom) {
        			y += h;
        		}
        		else {
        			y += size;
        		}
        		
    	        bGr.drawString(text, x, y);    	        	
        	}	                
	        
        }
        
        bGr.dispose();
        
        File outputfile = new File(outputFile);
        String ext = outputFile.substring(outputFile.lastIndexOf('.') + 1).toLowerCase();
        ImageIO.write(bufferedImage, ext, outputfile);
        
        System.out.println("display saved to " + outputfile);
        
	}
	
	
	public static void exec (String [] args)
	{
		
		try {
		
			ArrayList<String> labels = new ArrayList<String>();
			HashMap<String, String> params = new HashMap<String, String>();
			
			int width = -1;
			int height = -1;
			
			String pathname = "display.txt";
			String outfile = "output.png";
			
			for (int i = 0; i < args.length; i ++) {
				if (args[i].equals("-w")) {
					width = Integer.parseInt(args[++i]);
				}
				else if (args[i].equals("-h")) {
					height = Integer.parseInt(args[++i]);
				}
				else if (args[i].equals("-s")) {
					String [] x = args[++i].toLowerCase().split("x");
					width = Integer.parseInt(x[0]);
					height = Integer.parseInt(x[1]);
				}
				else if (args[i].equals("-i")) {
					pathname = args[i];
				}
				else if (args[i].equals("-o")) {
					outfile = args[i];
				}
				else if (args[i].equals("-S"))
				{
					InputStream in_ = Payload.class.getResourceAsStream(
							"/com/s5tech/net/services/display/EPaperPayloadExample.txt");			
					BufferedReader in = new BufferedReader(new InputStreamReader(in_));
					for ( ;;) {
						String l = in.readLine();
						if (l == null) break;
						System.out.println(l);					
					}
					System.out.println();					
					System.exit(0);
				}
			}
			
			File f = new File(pathname);
			
			if (f.exists()) {
				
				System.out.println("reading definitions from file " + pathname);
				
				BufferedReader in = new BufferedReader(new FileReader(f));
				
				for (;;) 
				{
					String txt = in.readLine();
					
					if (txt == null) break;
					
					txt = txt.replace('\t', ' ').trim();
					if (txt.length() == 0) continue;
					if (txt.startsWith("#") || txt.startsWith("//")) continue;

					if (txt.startsWith("@def")) 
					{
						try {
							String key = null;
							String value = null;
							for (String tk : txt.split(" ")) {
								if (tk.startsWith("@def")) continue;
								if (tk.trim().length() > 0) {
									key = tk.trim();
									break;
								}
							}
							if (key != null) {
								value = txt.substring(txt.indexOf(key) + key.length()).trim();						
								if (key != null && value != null && value.length() > 0) {
									params.put(key, value);
								}
							}
						}
						catch (Throwable t) {
							t.printStackTrace();
						}
					}
					else 
					{
						labels.add(txt);
					}
				}
				in.close();
				
			}
			
			if (labels.size() == 0) 
			{
				System.err.println ("No labels defined... printing an example!");
				labels.add ("Arial;   bold; 12;   0;   0; EMPTY LABEL");
			}
			
			new GeneratePngDisplay().create(labels, params, outfile, width, height);
			
			System.exit(0);
        
		}
		catch (Throwable t) {
			
			t.printStackTrace();
			System.exit(1);
			
		}
		
	}	

}

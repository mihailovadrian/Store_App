package tools;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class ResizeImage
{
	public static BufferedImage resizeImage( BufferedImage originalImage, Integer img_width, Integer img_height )
	{
		int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

		BufferedImage resizedImage = new BufferedImage(img_width, img_height, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, img_width, img_height, null);
		g.dispose();

		return resizedImage;
	}
}

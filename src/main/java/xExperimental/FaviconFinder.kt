package xExperimental

//fun URL.getImage(): BufferedImage {
//    fun drawCenteredString(color: Color, g: Graphics, text: String, rect: Rectangle, font: Font) {
//        // Get the FontMetrics
//        val metrics = g.getFontMetrics(font)
//        // Determine the X coordinate for the text
//        val x = rect.x + (rect.width - metrics.stringWidth(text)) / 2
//        // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
//        val y = rect.y + (rect.height - metrics.getHeight()) / 2 + metrics.getAscent()
//        // Set the font
//        g.setColor(color)
//        g.setFont(font)
//        // Draw the String
//        g.drawString(text, x, y)
//    }
//
//    fun betterNiceColor(): Color {
//        val goldenRatioConj = (1.0 + Math.sqrt(5.0)) / 2.0
//        var hue = Random().nextFloat()
//        hue += (goldenRatioConj * (0 / (5 * Math.random()))).toFloat()
//        hue = hue % 1
//        return Color.getHSBColor(hue, 0.5f, 0.95f)
//    }
//
//    fun betterNiceColor2(): Color {
//        var random = Random();
//        var hue = random.nextFloat()
//        // Saturation between 0.1 and 0.3
//        var saturation = (random.nextInt(5000) + 1000) / 10000f
//        var luminance = 0.9f
//        return Color.getHSBColor(hue, saturation, luminance)
//    }
//
//    fun betterNiceColorGray(): Color {
//        var random = Random();
//        var hue = random.nextFloat()
//        // Saturation between 0.1 and 0.3
//        var saturation = 0f
//        var luminance = (random.nextInt(3500) + 3500) / 10000f
//        return Color.getHSBColor(hue, saturation, luminance)
//    }
//
//    fun getContrastColor(color: Color): Color {
//        val red = color.red
//        val green = color.green
//        val blue = color.blue
//        val lum = 0.299 * red + (0.587 * green + 0.114 * blue)
//        return if (lum > 186) Color.black else Color.WHITE
//    }
//
//    fun process(text: String, img: BufferedImage): BufferedImage {
//        val g2d = img.createGraphics()
//        val bgColor = betterNiceColorGray()
//        g2d.setPaint(bgColor)
//        g2d.fillRect(0, 0, img.getWidth(), img.getHeight())
//        var font = Font(SANS_SERIF, PLAIN, (img.height * 0.7).toInt())
//        drawCenteredString(getContrastColor(bgColor), g2d, text, Rectangle(0, 0, img.width, img.height), font)
//        g2d.dispose()
//        return img
//    }
//
//     fun imageWithLetter(letter: Char): BufferedImage {
//        return process(letter.toString(), BufferedImage(256, 256, BufferedImage.TYPE_4BYTE_ABGR));
//    }
//
//    fun getFrom(url: URL): BufferedImage {
//        // TODO https://stackoverflow.com/questions/21991044/how-to-get-high-resolution-website-logo-favicon-for-a-given-url
//        /*
//            Do-it-yourself algorithm
//            Look for Apple touch icon declarations in the code, such as <link rel="apple-touch-icon" href="/apple-touch-icon.png">. Theses pictures range from 57x57 to 152x152. See Apple specs for full reference.
//            Even if you find no Apple touch icon declaration, try to load them anyway, based on Apple naming convention. For example, you might find something at /apple-touch-icon.png. Again, see Apple specs for reference.
//            Look for high definition PNG favicon in the code, such as <link rel="icon" type="image/png" href="/favicon-196x196.png" sizes="196x196">. In this example, you have a 196x196 picture.
//            Look for Windows 8 / IE10 and Windows 8.1 / IE11 tile pictures, such as <meta name="msapplication-TileImage" content="/mstile-144x144.png">. These pictures range from 70x70 to 310x310, or even more. See these Windows 8 and Windows 8.1 references.
//            Look for /browserconfig.xml, dedicated to Windows 8.1 / IE11. This is the other place where you can find tile pictures. See Microsoft specs.
//            Look for the og:image declaration such as <meta property="og:image" content="http://somesite.com/somepic.png"/>. This is how a web site indicates to FB/Pinterest/whatever the preferred picture to represent it. See Open Graph Protocol for reference.
//            At this point, you found no suitable logo... damned! You can still load all pictures in the page and make a guess to pick the best one.
//        */
//
//        return imageWithLetter(InternetDomainName.from(url.host).topPrivateDomain().toString().toUpperCase()[0])
//    }
//
//    return getFrom(this)
//}

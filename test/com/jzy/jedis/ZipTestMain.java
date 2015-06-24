package com.jzy.jedis;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class ZipTestMain {
	// 压缩
	  public static String compress(String str) throws IOException {
	    if (str == null || str.length() == 0) {
	      return str;
	    }
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    GZIPOutputStream gzip = new GZIPOutputStream(out);
	    gzip.write(str.getBytes());
	    gzip.close();
	    return out.toString("ISO-8859-1");
	  }
	 
	  // 解压缩
	  public static String uncompress(String str) throws IOException {
	    if (str == null || str.length() == 0) {
	      return str;
	    }
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    ByteArrayInputStream in = new ByteArrayInputStream(str
	        .getBytes("ISO-8859-1"));
	    GZIPInputStream gunzip = new GZIPInputStream(in);
	    byte[] buffer = new byte[256];
	    int n;
	    while ((n = gunzip.read(buffer)) >= 0) {
	      out.write(buffer, 0, n);
	    }
	    // toString()使用平台默认编码，也可以显式的指定如toString("GBK")
	    return out.toString();
	  }

	public static void main(String[] args) {
		try {

			String str = "0:0:3:33317:p54wydBzYkv3ulQ3YY7XSjlqjWnEZKiWOVr1Ror6fLPmZxanRu"       +
					"1:0:3:38418:AsWaJOKGz9rfMrydKWcVVSVAKqd3bGJo8uRBJVNrnR55OWUTfX"       +
					"2:0:3:2589:m5GbXfTOJIAU3WhtnQpGzYRT2IUIFE4vdbN21bSAnZJ9vGH4Ka"        +
					"3:0:3:1567:AOZH7zoRWfiS08fS8FUM3YAEQ7xglfArJL8botAz1L6DVU6N08"        +
					"4:0:3:49015:5t9roaPJUiqtRXmo7ap4pgfUxal4g4phvxnx4GMt02EqLFBc40"       +
					"5:0:3:3215:mr9zxTgwReTMU9s1hdOFhH3mNwok5cK7DS17UmHdE6VTQDQ8P3"        +
					"6:1:3:16158:Icb6SjCckGZcHGBkati5ruSPStfqeQGTab6TQ5tc7XFUEfcIUA"       +
					"7:0:3:1426:IjEz5Hm2kuNal9gVH8quV4DFcozychZzILBcywbKbyyoI3wBpO"        +
					"8:1:3:31036:t6QcgVzpLzAvH7AistGl1IAgM2nY4aWcAxeP0wQN0sByg8sAML"       +
					"9:0:3:18826:hUXEEnBXnWQhsL6wYvv6NexKBKOTi2ngVp5PIAzUGC2BssNn6U"       +
					"10:0:3:7219:WPNDYIYKNv4L4410Ac2n9n6I2Q5vQQPtBUX9eyE4abndIplyEk"       +
					"11:1:3:24760:NqcXakmJuBXBmYF9xJg7EOweknU9VasuUd5OYRV9lK1cvLFYI9"      +
					"12:0:3:7738:Zmp9vkgIftu3EWX8uVktLxAdUoEHNxBWOfXFOoj1gZt5uZdBK4"       +
					"13:1:3:24324:SaBsijD310grutsnUKHBPGGFcFAewYoYuS5eGmYF3Onir2q9jK"      +
					"14:0:3:8612:Yk1RuxVkj1wiBSmC0Yej4Y5sydd4yHx4oGXfrd0AODxO3wNKUr"       +
					"15:1:3:31588:cxRyfwbVnuw9w9VxgE8TOJDMJu8snEPw0zawnMuNJ9jPyjYWFc"      +
					"16:0:3:26899:MrOGYZm5LF0hrsQHKYEO3vX3rPfGxQEffOEAHaPp8EhTrBEueK"      +
					"17:2:3:30743:OnwSUbokMAEs250KW0G7TZdHM0WYZzBM5zP2tWHsQrvNZgxvht"      +
					"18:1:3:28369:af02gPtDsugUKKcOBJz0oNpTQVPy62cym2uSbHe8XMXoMCqrbg"      +
					"19:1:3:28273:GTIxpv9lHTktVK4R1kppH3aNak0edmXoe6E9hCY0aLjpqjzMHZ"      +
					"20:2:3:37984:kSAdaZ8GXDDbYLSTxJbU7HPgfY1dhgMkSw7ylbcx3OtdSslqkY"      +
					"21:0:3:21399:jZ9PsevKhjf6LseudUjjzk905gC6eLcJSLoAPFyfLXRkbZFNj1"      +
					"22:2:3:13263:slv9XfUi0SB8Ci3WtLu39cVmA9KtKAOsrByZ8SZC1lcjTREVuC"      +
					"23:1:3:20474:fmpxvk4ScTDFLV079NkXz4n0IMcrLhov0S3fyDTeEIxJRIfYRH"      +
					"24:2:3:36633:WGfKoNrnMq7sbQWqEOOCfajbpAg8Raw6SRcVsbgI4utWklfH5u"      +
					"25:0:3:8779:BKIg01UYA7UsNHQit3z9MKJFDN2R6VJccmeLXcjL2dttwrDjJi"       +
					"26:2:3:46368:x8PGypvDBC6zcoAZhZorVfDl2LJzkX7huxqqzPOTFNdpHoToTw"      +
					"27:1:3:13125:qLq7x3Hvgt2CpOq4vNzZWDCF4CMyZOeKEoEFuDSJ1zC8cpV8D7"      +
					"28:2:3:48912:kulDPTvpQmVXU346fj9AblWalpzCbGmxZLSU9GOn7UvvBuUYPz"      +
					"29:0:3:41651:Y4G0Kf6zRQxZpY454E1Yhzcsu1Q9t29fxL3905ztvfjuzjE7ht"      +
					"30:2:3:38699:6cSlcppoPT8UygNnyqdgeTwp2TyIyVjeivVHwtwWpLc6vyzXDW"      +
					"31:1:3:49981:lTcTBp6DpUbSAZnnxITWtOkORiwvPGnacBGb7bIFYKAYhybFXb"      +
					"32:1:3:40381:sjshbNriRJxlAvbSDhJdkyJyiThs332r0jEU8Y9Y906GTsZNJW"      +
					"33:1:3:1637:DefJ7Pcs053UeDWEp18MoQi2oTcGMdBkQC0jFUl7WxCA1DXLt2"       +
					"34:1:3:43674:OULMn0meml8BFhMBxOUHsnkrCjCUUUkPW7kcb4m27KA1PBXawN"      +
					"35:1:3:32250:mxYHhJUcdXCrm7GeHogyQEaQPSUg8H7ZF9edfbETHg6tRMa5ov"      +
					"36:1:3:38260:4kVrWiFR4DfI8slzjzG2KBptfqsy8TUjcCc3NjpCREfPxOqim8"      +
					"37:1:3:12551:oc8sVDljXcOVg415L0x1gzkbnXogOt50MJeyAGNHA4w6bTezQ4"      +
					"38:1:3:24226:4T2rwUnY7Yu53lRZm7WoRvYGbZufUCg3i9J4kRhwaBkk7Hlz4c"      +
					"39:1:3:29279:nXuwh7S6tWw8hCvzKWNc5Y2251wAixCX6p8s3BPqwrbx2fDBCL"      +
					"40:1:3:33699:jMWSHQE7aYH1i7KOG8o6dshlfhTkcbFRBVhCqoBeM26eS1eWvY"      +
					"41:1:3:11371:KJ61zvOtOG0a3WNNwbXW3pW2PU6YYTEyPyTqv4Jr2yEIB1Ooxp"      +
					"42:3:3:17790:3rKH1i63TgMhEOHZodoiuXzEL2or3ISqkcyeg2de1gO2aLnCoF"      +
					"43:2:3:44080:alCe8p1ZJEGKAjQd3NCSe1lbagNrSE7pfwXtOiLh4RyK20nzFb"      +
					"44:0:3:1575:EJPclkSqSZXN5eX2w2dmDpv4p64JYeMYaBIYsqbou0RJMBsIZ0"       +
					"45:2:3:38626:jJHeKuvwgh1YlT6M7NGKpgah7ZEHotX5IFAd5u50zWh8NT3E8v"      +
					"46:3:3:31284:OvXJnvhzb4MHZFPa6hG2Y02wYR2CZhB21Haq9wGJdHKlaHjqQD"      +
					"47:4:3:32670:KfD9RCsnYDGpfbfh0zDrUK6Dt5rrmCmMhabJyaa895pjq6IWIJ"      +
					"48:2:3:17119:XsLEkrYSDWkJkEAYhctuYsF72ZEZg8qX0nw5ZMb1fxlz6caEMA"      +
					"49:0:3:1614:naPkGHK9ihXGCYkZTcFQNiEb4YqLDk93pF1HB21KL4rGAFU4hf"       +
					"50:2:3:21897:7kptrBq0GR1yNkbLuRdRfbNjsYsEk1zIlFpuZhDxM7ZN5f9KNo"      +
					"51:4:3:32065:GMx2iIjNQlUBo5Ii9n7sdKbniAoW4lmTWKdyyZhcxHv0RHcS04"      +
					"52:4:3:27080:9OXP9QrQKBYsYStFBtCueBOVaYPLMOyfI1oWjIw0kBVFVWbYcP"      +
					"53:4:3:41401:b51U8w2kqcMqMwwvWaxD5gYcrtoaGuB2HNTZjlQGsEFzf3kofP"      +
					"54:4:3:46944:RwnxfzUiZ8VWrTZlcJTglsJyO8hy69VuWDtJrxamaHVh7U5zDf"      +
					"55:4:3:337:fqEWnXhorjvgXNmwLxrGxgvGU7LljOWPHqTOoBSWHWfxyKKwFG"        +
					"56:4:3:26935:mWQ8OgIlI2abfZm2KIy4hWMf8zMFFafAXEK09iyGqDQxOjjFcl"      +
					"57:3:3:36443:2FqhHoHLFWZQ1tOeuuMjRuWtPIbiaw7jsBHUYxJrYcmIr7Dcjz"      +
					"58:3:3:5805:ThCtw6gi23UHcQa8IUjJ1EwkcDEIUKWTgrRyZw8rx8nqH6nwKc"       +
					"59:4:3:15488:18GabSLrfxLIp4QwCXnLlIUP7KzhnjGClw3HjAhcANpmKv4bOa"      +
					"60:2:3:4197:7pxtR1rfiuyrOFpehR4UO2FfB1ygkVUXrwVCgma1TjJk3GwrOX"       +
					"61:0:3:46476:XMVqXTG10tX0mJC7SOFTpQjaVgQMXEgRfzJspmDmQFr1KPbCqJ"      +
					"62:2:3:26296:G7myljFoN6wBslW3ALCRLsMC7tXKXplsHoPE1EQ4RMnroLgHbO"      +
					"63:4:3:38887:ifwP7wnTKKjuxhmXG65hOdYnJrP9GRTcHSFWx6UUxg4fRX47hq"      +
					"64:4:3:12964:1RbhKWBHBwYryxmEKNUoCbxFtr6jD9SHTq3WluDqHS2jXSOJYE"      +
					"65:4:3:45783:RyN07I7esaMmKcIkEWlcXvTWhhyZLKhNZRJGBFNajSGNnVXBjX"      +
					"66:4:3:37965:QZ6tY9QV7e6ZO2ElEU0SgDl3v4ypnhoqITTw74Tt55lQAzOi0L"      +
					"67:3:3:29996:uL315Kc8eRHh3iRzlep48mQsWQMpcfhuR4WmC3KTpkmdTdLUGk"      +
					"68:4:3:12000:npmjlmbYPiIcVjhCQJO8VD4amF91YDwY8GdQ6TAEq0P52LuO3e"      +
					"69:2:3:47942:UuTIFGkxT6YVIkfNE574nRkcDy2X8I6teSGLQ9lTjYRitg9Qcj"      +
					"71:0:3:13997:iD1WoTtBDwg6RbNNOwt9lp3i3rlDZYTKP4perlrHYmBeGQ2T8m"      +
					"73:2:3:24512:sitlckO7qDe2WtKSml95hxltTlaQyHFL51WEKaXRLEj3EfNeGw"      +
					"74:4:3:43807:yK1ArRogYVcJdbNAycyFwLsoxL58cc64fUrO0jZOUtTV6MVbga"      +
					"75:4:3:49084:PKv2hNawLUoft7QM3wlP0VmN4wYrT22FA2NhmytrbiBlmGLOS5"      +
					"76:4:3:19309:zlFkIqbHyVISk25UtkapNjgHr4hB6CZWlXRdTWNKq6fH03xa2j"      +
					"77:3:3:11964:jswJsGvp8m76iUNZcabszmZvNv0ndScdHKQqCzKSQrYPCx8khG"      +
					"78:4:3:29056:DZfmU3dNGLpotVqT5GjJPlpPRJ5AoMEPVOweuiAzIIJ94hfL2Y"      +
					"80:2:3:46394:E9ZqsGpgPZ2lKYbsMEekXp2kzWcqjYuZ8fNQReYrTUx5r2hxFS"      +
					"82:0:3:3320:D72z0pfA8wBcm78JOtUkhuU02po1W9CFfaSRv6Rr6A2sl6UR6U"       +
					"84:2:3:31601:Wsz5OTmRo8xNMUs0Ky0Qqbs4owEpBVcklajlne1CFabW7oz2TJ"      +
					"86:4:3:32141:dbZX3q5bSM9YY39NDX9kQMX0m1X7xGjQJ4uzkuKGEV060C27si"      +
					"87:3:3:40530:vp7H5IXwZNxEwP1anAivmYheib6fbDmVy4kJ1SttLgIZuw5drk"      +
					"88:4:3:7614:5xPu9eCgnRHxdSuEkA0reBKJxO771288NtL6A1UuE2yTdbUEtV"       +
					"90:2:3:38435:0ZwQrFggYMBgz0lJZyjgsFbbLT6lwBmHiob2tBHZRz8NdvFiWE"      +
					"93:2:3:41003:6nrkcxsJ5KtrgUAvsLhfIyjz5NOwv1uCDORWdNG1ALUhxFGrrG"      +
					"95:4:3:2153:6akRBkSLD1r83JyyHkLTh30ApYMAJnEJwBXpa6peauiyTvuTpG"       +
					"96:3:3:43517:nvSCjUF3t9UgtVo0eVgCGhC0mIDlP9LW4LaQc0Q1C49dBmoYwo"      +
					"98:2:3:11546:iBhwVmRowFku729AruiD9yOKGloIg2z2lsirJ2HEkigkRoMvgB"      +
					"100:0:3:10863:QrYxVjKfZYKsQKWBIYB8ZqsBh8VScEf82IeBwlxGpSz1BGdX2p"     ;
			String comp = compress(str.substring(0, 1200));
			System.out.println(comp.length());
			System.out.println(uncompress(comp).length());
			
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}

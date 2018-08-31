function getRGB(bh){
	alert("getRGB");
	var param = "/rsui/ImageRGB/GETRGB.jsp?bh=" + bh;
	param = encodeURI(param);
	param = encodeURI(param);
	window.open(param,	'_blank','resizable=yes,width=800px,height=800px,top='+(screen.height-800)/2+',left='+(screen.width-800)/2+',toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes');
}
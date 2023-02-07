package oasis.team.econg.graduationproject

class GpsTransfer(lat: Double, lng: Double) {
    private var lat: Double = lat
    private var lng: Double = lng
    private var xLat: Double = 0.0
    private var yLng: Double = 0.0

    fun getXLat(): Double{
        return xLat
    }
    fun getYLng(): Double{
        return yLng
    }

    fun transfer(){
        val RE = 6371.00877
        val GRID = 5.0
        val SLAT1 = 30.0
        val SLAT2 = 60.0
        val OLNG = 126.0
        val OLAT = 38.0
        val XO = 43
        val YO = 136

        val DEGRAD = Math.PI / 180.0
        val RADDEG = 180.0 / Math.PI

        val re = RE/GRID
        val slat1 = SLAT1 * DEGRAD
        val slat2 = SLAT2 * DEGRAD
        val olng = OLNG * DEGRAD
        val olat = OLAT * DEGRAD

        var sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5)
        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn)
        var sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5)
        sf = Math.pow(sf, sn) * Math.cos(slat1) / sn
        var ro = Math.tan(Math.PI * 0.25 + olat * 0.5)
        ro = re * sf / Math.pow(ro, sn)
        /////////////
        var ra = Math.tan(Math.PI * 0.25 + lat * DEGRAD * 0.5)
        ra = re * sf / Math.pow(ra, sn)
        var theta = lng * DEGRAD - olng

        if(theta > Math.PI) theta -= 2.0 * Math.PI
        if(theta < -Math.PI) theta += 2.0 * Math.PI
        theta *= sn

        xLat = Math.floor(ra * Math.sin(theta) + XO + 0.5)
        yLng = Math.floor(ro - ra * Math.cos(theta) + YO + 0.5)

    }
}
export default {
    parse(gpsString){
        if(_isGpsString(gpsString) === false){
            throw new Error(`${gpsString}은 GPS String 포맷에 맞지 않습니다.`)
        }

        let splitedList = gpsString.split("!@");
        let lat = Number.parseFloat(splitedList[2]);
        let lng = Number.parseFloat(splitedList[3]);

        return {
            lat,
            lng
        }
    },

    stringify(gpsObject){
        if(typeof gpsObject.lat !== "number" || typeof gpsObject.lng !== "number"){
            throw new Error(`lat나 lng 중 숫자가 아닌 값이 있습니다.`);
        }

        return `!@GPS!@${gpsObject.lat}!@${gpsObject.lng}`;
    },

    isGpsString(string){
        return _isGpsString(string)
    }
}


function _isGpsString(gpsString){
    if(gpsString[0] !== "!" || gpsString[1] !== "@"){
        return false;
    }

    let splitedList = gpsString.split("!@");

    if(splitedList.length !== 4){
        return false;
    }

    if(splitedList[1] !== "GPS"){
        return false;
    }

    if(isNaN(Number.parseFloat(splitedList[2]))){
        return false;
    }

    if(isNaN(Number.parseFloat(splitedList[3]))){
        return false;
    }

    return true;
}
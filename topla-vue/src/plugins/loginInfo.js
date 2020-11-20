export default {
    setLoginInfo(loginInfoString){
        window.localStorage.setItem("loginInfo", loginInfoString);
    },

    getLoginInfo(){
        let loginInfo = window.localStorage.getItem("loginInfo");

        if(loginInfo === null || loginInfo === null){
            throw new Error("로그인 정보 없습니다.");
        }

        return loginInfo;
    },

    isThereLoginInfo(){
        let loginInfo = window.localStorage.getItem("loginInfo");

        if(loginInfo === null || loginInfo === null){
            return false;
        }

        return true;
    }
}
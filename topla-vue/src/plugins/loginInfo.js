export default {
    setLoginInfo(loginInfoString){
        window.localStorage.setItem("loginInfo", loginInfoString);
    },

    getLoginInfo(){
        let loginInfo = window.localStorage.getItem("loginInfo");

        if(loginInfo === null || loginInfo === undefined){
            throw new Error("로그인 정보 없습니다.");
        }

        return loginInfo;
    },

    isThereLoginInfo(){
        let loginInfo = window.localStorage.getItem("loginInfo");

        if(loginInfo === null || loginInfo === undefined){
            return false;
        }

        return true;
    },

    clearLoginInfo(){
        window.localStorage.removeItem("loginInfo");
    },

    setUserEmail(userEmail){
        window.localStorage.setItem("userEmail", userEmail);
    },

    getUserEmail(){
        let loginInfo = window.localStorage.getItem("userEmail");

        if(loginInfo === null || loginInfo === undefined){
            throw new Error("로그인 정보 없습니다.");
        }

        return loginInfo;
    },

    isThereUserEmail(){
        let loginInfo = window.localStorage.getItem("userEmail");

        if(loginInfo === null || loginInfo === undefined){
            return false;
        }

        return true;
    },

    clearUserEmail(){
        window.localStorage.removeItem("userEmail");
    },

    clearAll(){
        this.clearLoginInfo();
        this.clearUserEmail();
    }
}
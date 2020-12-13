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

    setSuperUserFlag(superUserFlag){
        if(superUserFlag !== true && superUserFlag !== false){
            throw new Error("superUserFlag는 boolean값이여야 함.");
        }

        console.log("set시도 값", superUserFlag);
        if(superUserFlag === true){
            console.log("set된 값", true);
            window.localStorage.setItem("superuser", "true");
        }
        else {
            console.log("set된 값", false);
            window.localStorage.setItem("superuser", "false");
        }
    },

    getSuperUserFlag(){
        let loginInfo = window.localStorage.getItem("superuser");

        if(loginInfo === null || loginInfo === undefined){
            throw new Error("로그인 정보 없습니다.");
        }

        if(loginInfo === "true"){
            return true;
        }

        return false;
    },

    isThereSuperUserFlag(){
        let loginInfo = window.localStorage.getItem("superuser");

        if(loginInfo === null || loginInfo === undefined){
            return false;
        }

        return true;
    },

    clearSuperUserFlag(){
        window.localStorage.removeItem("superuser");
    },

    clearAll(){
        this.clearLoginInfo();
        this.clearUserEmail();
        this.clearSuperUserFlag();
    }
}
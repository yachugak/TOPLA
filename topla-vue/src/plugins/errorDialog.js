let isDevMode = true;
export default function(vueInstance, title, errorObject){
    if(errorObject === null || errorObject === null){
        vueInstance.$dialog.error({
            title: title,
            message: "알 수 없는 에러로 실패하였습니다."
        });
        return;
    }

    if(errorObject.response === undefined || errorObject.response === null){
        vueInstance.$dialog.error({
            title: title,
            message: "알 수 없는 클라이언트 에러로 인해 실패하였습니다."
        });
        if(isDevMode){
            console.log(errorObject);
        }

        return;
    }

    if(errorObject.response.message === undefined || errorObject.response.message === null || errorObject.response.message === ""){
        vueInstance.$dialog.error({
            title: title,
            message: "알 수 없는 서버 에러로 인해 실패하였습니다. 관리자에게 문의하세요."
        });

        return;
    }

    vueInstance.$dialog.error({
        title: title,
        message: errorObject.response.message
    });
}
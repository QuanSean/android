let jwt = require('jsonwebtoken');
let KEY = require('../Common/Constant/key');
// let crypto = require('crypto');
let gameCode = 1000;
//key = gameCode; val = idGame
let mapToken = new Map();
let mapIdGame = new Map();
let User = require('../Model/User');
module.exports = ({
    computingJWT: (email, callback) => {
        let payload = {
            "email": email,
            "confess": KEY.CONFESS,
            "key": "your name (full)",
            "exp": Date.now() + 43200000
        }
        //exp in 12h
        secretKey = KEY.SECRET;
        jwt.sign(payload, secretKey, { algorithm: 'HS256' }, callback);
    },
    getToken: (email) => {
        return mapToken.get(email);
    },
    addNewTokenForUser: (email, token) => {
        let arrToken = mapToken.get(email);
        if (!arrToken) {
            arrToken = [];
        }
        arrToken.push(token);
        mapToken.set(email, arrToken);
        // mapToken.set(email, token);
    },
    removeTokenForUser: (email) => {
        mapToken.delete(email);
    },
    createMeetingCode: (idMeeting) => {
        mapCodeMeeting(++meetingCode, idMeeting);
        return meetingCode;
    },
    verifyToken: async (token, callback) => {
        let decodedToken = await jwt.decode(token, KEY.SECRET);
        if (decodedToken) {
            if (decodedToken.exp < Date.now()) {
                mapToken.delete(decodedToken.email);
                return callback(new Error(ERROR.TOKEN.EXPIRED), null);
            } else {
                let tokenByEmail = await mapToken.get(decodedToken.email);
                if (tokenByEmail) {
                    if (tokenByEmail.indexOf(token) >= 0) {
                        User.findOne({
                            email: decodedToken.email
                        }, (err, user) => {
                            if (err) {
                                return callback(new Error(ERROR.ACCOUNT.NOT_EXIST), null);
                            } else {
                                if (user) {
                                    return callback(null, user);
                                }
                            }
                        })
                    } else {
                        return callback(new Error(ERROR.TOKEN.INVALID), null);
                    }
                }
            }
        } else {
            return callback(new Error(ERROR.TOKEN.INVALID), null);
        }
    },
    createGameCode: (idGame) => {
        let code = ++gameCode;
        mapIdGame.set(code, idGame);
        return code;
    },
    getIdGame: (code) => {
        return mapIdGame.get(code);
    },
    removeGameCode: (code) => {
        mapIdGame.delete(code);
    }
})
import axios from "axios";
import { SET_CURRENT_USER, GET_ERRORS } from "./types";
import setJwToken from "../securityUtils/setJwToken";
import jwt_decode from "jwt-decode";

export const createNewUser = (newUser, history) => async (dispatch) => {
  try {
    await axios.post("/api/users/register", newUser);
    history.push("/login");
    dispatch({
      type: GET_ERRORS,
      payload: {},
    });
  } catch (err) {
    dispatch({
      type: GET_ERRORS,
      payload: err.response.data,
    });
  }
};

export const login = (LoginRequest) => async (dispatch) => {
  try {
    // post login req
    const res = await axios.post("/api/users/login", LoginRequest);
    // extract token
    const { token } = res.data;
    // store token in localStorage
    localStorage.setItem("jwtToken", token);
    // set token in headers
    setJwToken(token);
    // decode token in react
    const decoded = jwt_decode(token);
    // dispatch to our security reducer
    dispatch({
      type: SET_CURRENT_USER,
      payload: decoded,
    });
  } catch (err) {
    dispatch({
      type: GET_ERRORS,
      payload: err.response.data,
    });
  }
};

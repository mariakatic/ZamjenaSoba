import { createContext } from "react";

// export const LoginContext = createContext({});
// export const TokenContext = createContext({});
// export const RoleContext = createContext({});



import React, { useReducer, useEffect } from "react";

let reducer = (info, newInfo) => {
  if (newInfo === null) {
    localStorage.removeItem("info");
    return initialState;
  }
  return { ...info, ...newInfo };
};

const initialState = {
  loggedIn: false,
  token: "",
  role: false
};

const localState = JSON.parse(localStorage.getItem("info"));

const InfoContext = React.createContext();

function InfoProvider(props) {
  const [info, setInfo] = useReducer(reducer, localState || initialState);

  useEffect(() => {
    localStorage.setItem("info", JSON.stringify(info));
  }, [info]);

  return (
    <InfoContext.Provider value={{ info, setInfo }}>
      {props.children}
    </InfoContext.Provider>
  );
}

export { InfoContext, InfoProvider };

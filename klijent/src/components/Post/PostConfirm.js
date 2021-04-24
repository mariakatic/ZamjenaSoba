import React, { useContext, useEffect, useState } from "react";
import { Card, Avatar, notification, Button } from "antd";
import axios from "axios";

import ReactTooltip from "react-tooltip";

// import { TokenContext } from "../../Context";
// import { LoginContext } from "../../Context";
import { InfoContext } from "../../Context";

import { CheckCircleOutlined, CloseCircleOutlined } from "@ant-design/icons";

const { Meta } = Card;

var MappleToolTip = require("reactjs-mappletooltip");

function Post(props) {
  const title = "Dom: " + props.title;
  //const description = props.description;
  const idOglas = props.idOglas;
  const kategorija_sobe = props.kategorija_sobe;
  const kat = props.kat;
  const paviljon = props.paviljon;
  const idLanac = props.idLanac;

  const { info, setInfo } = useContext(InfoContext);
  
  // const { token, setToken } = useContext(TokenContext);
  // const { loggedIn, setLoggedIn } = useContext(LoginContext);
  const [like, setLike] = useState();

  const [like1, setLike1] = useState(false);
  const [like2, setLike2] = useState(false);
  const [like3, setLike3] = useState(false);

  const likeErrorNotification = () => {
    notification["error"]({
      message: "Greška",
      description: "Došlo je do greške prilikom potvrde zamjene.",
    });
  };

  const likeHandler = async (oglasId, stupanjLajkanja) => {
    let values = {
      oglasId: oglasId,
      stupanjLajkanja: stupanjLajkanja,
    };

    try {
      await axios
        .post(`${process.env.REACT_APP_API_URL}/api/home/`, values, {
          headers: {
            Authorization: "Bearer " + info.token, //the token is a variable which holds the token
          },
        })
        .then((res) => {
          switch (res.data.stupanjLajkanja) {
            case 1:
              setLike1(true);
              setLike2(false);
              setLike3(false);
              break;
            case 2:
              setLike1(false);
              setLike2(true);
              setLike3(false);
              break;

            case 3:
              setLike1(false);
              setLike2(false);
              setLike3(true);
              break;

            case 0:
              setLike1(false);
              setLike2(false);
              setLike3(false);
              break;

            default:
              break;
          }
        });
    } catch (error) {
      likeErrorNotification();
    }
  };

  const setLikeFromAPI = (lajk) => {
    if (lajk == 1) {
      setLike1(true);
    }

    if (lajk == 2) {
      setLike2(true);
    }

    if (lajk == 3) {
      setLike3(true);
    }
  };

  const Remove = () => {
    axios
      .post(
        `${process.env.REACT_APP_API_URL}/api/home/`,
        {
          stupanjLajkanja: "4",
          oglasId: idOglas,
        },
        {
          headers: {
            Authorization: "Bearer " + info.token, //the token is a variable which holds the token
          },
        }
      )
      .then((res) => {
        console.log(res);
      });
  };

  const ConfirmSwap = async (oglasId) => {
    let values = {
      idLanca: idLanac,
      //oglas: oglasId,
    };
    try {
      await axios
        .post(
          `${process.env.REACT_APP_API_URL}/api/v1/potvrdiZamjenu`,
          values,
          {
            headers: {
              Authorization: "Bearer " + info.token, //the token is a variable which holds the token
            },
          }
        )
        .then((res) => {
          console.log(res.data);
          notification.open({
            message: "Uspješno potvrđena zamjena",
            description:
              "Ukoliko se zamjena bude uspješna dobiti ćete mail sa potvrdom o uspješnoj zamjeni",
          });
        });
    } catch (error) {
      likeErrorNotification();
    }
  };
  const RejectSwap = async (oglasId) => {
    let values = {
      idLanca: idLanac,
      //oglas: oglasId,
    };
    try {
      await axios
        .post(`${process.env.REACT_APP_API_URL}/api/v1/odbijZamjenu`, values, {
          headers: {
            Authorization: "Bearer " + info.token, //the token is a variable which holds the token
          },
        })
        .then((res) => {
          console.log(res.data);
          notification.open({
            message: "Uspješno odbačena zamjena",
            // description: "",
          });
        });
    } catch (error) {
      likeErrorNotification();
    }
  };

  useEffect(() => {
    setLikeFromAPI(props.lajk);
  }, []);

  return (
    <div>
      {info.loggedIn ? (
        <Card
          style={{ maxWidth: "720px", margin: "auto" }}
          actions={[
            <div onClick={() => ConfirmSwap(idOglas)}>
              <CheckCircleOutlined /> Potvrdi
            </div>,
            <div onClick={() => RejectSwap(idOglas)}>
              <CloseCircleOutlined /> Odbij
            </div>,
          ]}
        >
          <Meta
            title={title}
            description={
              <React.Fragment>
                <p>Paviljon:{paviljon}</p>
                <p>Kat:{kat}</p>
                <p>Kategorija sobe:{kategorija_sobe}</p>
              </React.Fragment>
            }
          />
        </Card>
      ) : (
        <Card style={{ maxWidth: "720px", margin: "auto" }}>
          <Meta
            title={title}
            description={
              <React.Fragment>
                <p>Paviljon: {paviljon}</p>
                <p>Kat: {kat}</p>
                <p>Kategorija sobe: {kategorija_sobe}</p>
              </React.Fragment>
            }
          />
        </Card>
      )}

      <br />
    </div>
  );
}

export default Post;

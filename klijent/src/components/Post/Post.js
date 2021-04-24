import React, { useContext, useEffect, useState } from "react";
import { Card, Avatar, notification } from "antd";
import axios from "axios";

import ReactTooltip from "react-tooltip";



// import { LoginContext } from "../Context";
// import { TokenContext } from "../Context";
import { InfoContext } from "../../Context";

import { HeartOutlined, HeartFilled, DeleteOutlined } from "@ant-design/icons";

const { Meta } = Card;

var MappleToolTip = require("reactjs-mappletooltip");

function Post(props) {
  const title = "Dom: " + props.title;
  //const description = props.description;
  const idOglas = props.idOglas;
  const kategorija_sobe = props.kategorija_sobe;
  const kat = props.kat;
  const paviljon = props.paviljon;
  const username = props.username;
  const email = props.email;

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
      description: "Došlo je do greške prilikom lajkanja oglasa. Provjerite svoj aktivni oglas.",
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
      .then((res) => {});
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
            like1 ? (
              <div onClick={() => likeHandler(idOglas, "0")}>
                <HeartFilled />
              </div>
            ) : (
              <div onClick={() => likeHandler(idOglas, "1")}>
                <HeartOutlined />
              </div>
            ),

            like2 ? (
              <div onClick={() => likeHandler(idOglas, "0")}>
                <HeartFilled />
                <HeartFilled />
              </div>
            ) : (
              <div onClick={() => likeHandler(idOglas, "2")}>
                <HeartOutlined />
                <HeartOutlined />
              </div>
            ),

            like3 ? (
              <div onClick={() => likeHandler(idOglas, "0")}>
                <HeartFilled />
                <HeartFilled />
                <HeartFilled />
              </div>
            ) : (
              <div onClick={() => likeHandler(idOglas, "3")}>
                <HeartOutlined />
                <HeartOutlined />
                <HeartOutlined />
              </div>
            ),

            // <Button type="text" className="navButton">Ne prikazuj više</Button>,

            <div className="App">
              <p
                data-tip
                data-for="registerTip"
                onClick={() => Remove(idOglas)}
              >
                <DeleteOutlined />
              </p>

              <ReactTooltip id="registerTip" place="top" effect="solid">
                Ne prikazuj više
              </ReactTooltip>
            </div>,
          ]}
        >
          <Meta
            title={title}
            description={
              <React.Fragment style={{width: "100%"}}>
                <div style={{width: "70%", float:"left"}}>
                  <p>Paviljon:&nbsp;{paviljon}</p>
                  <p>Kat:&nbsp; {kat}</p>
                  <p>Kategorija sobe:&nbsp;{kategorija_sobe}</p>
                </div>

                <div style={{width: "30%", float:"right"}}>
                  <p>Korisničko ime:&nbsp;{username}</p>
                  <p>E-mail:&nbsp;{email}</p>
                </div>
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
                <p>Paviljon:&nbsp;{paviljon}</p>
                <p>Kat:&nbsp;{kat}</p>
                <p>Kategorija sobe:&nbsp;{kategorija_sobe}</p>
                <p>Korisničko ime:&nbsp;{username}</p>
                <p>E-mail:&nbsp;{email}</p>
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

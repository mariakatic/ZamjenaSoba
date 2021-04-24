import React, { useContext, useState, useEffect } from "react";
import { Layout } from "antd";
import { Button, Card, Form, notification } from "antd";
import { Link, useParams } from "react-router-dom";
import Post from "../components/Post/Post";
import axios from "axios";
import { UserOutlined } from "@ant-design/icons";
import NavBar from "../components/NavBar/NavBar";
import { CheckCircleOutlined, CloseCircleOutlined } from "@ant-design/icons";

// import { LoginContext } from "../Context";
// import { TokenContext } from "../Context";
import { InfoContext } from "../Context";

const { Content, Footer } = Layout;

export default function SCDjelatnikPage() {
  const { info, setInfo } = useContext(InfoContext);
  
  // const { loggedIn, setLoggedIn } = useContext(LoginContext);
  // const { token, setToken } = useContext(TokenContext);
  const [lanci, setLanci] = useState([]);

  const ConfirmSwap = async (idLanac) => {
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
          getSwaps();
        });
    } catch (error) {}
  };

  const getSwaps = () => {
    axios
      .get(`${process.env.REACT_APP_API_URL}/api/v1/zamjene`, {
        headers: {
          Authorization: "Bearer " + info.token, //the token is a variable which holds the token
        },
      })
      .then((res) => {
        // console.log(res.data);
        var uniqueIds = [];
        var uniqueIdsObject = [];

        if (res.data.length === 0) {
          //neš tu treba
        } else {
          res.data.forEach((objekt) => {
            // console.log(objekt);
            // console.log("(!uniqueIds.includes(objekt.idLanca)):");
            // console.log(!uniqueIds.includes(objekt.idLanca));
            // console.log("uniqueIds:");
            // console.log(uniqueIds);
            if (!uniqueIds.includes(objekt.idLanca)) {
              uniqueIds.push(objekt.idLanca);
              uniqueIdsObject.push({ idLanca: objekt.idLanca, oglasi: [] });
            }
          });

          res.data.forEach((objekt) => {
            for (var i = 0; i < uniqueIdsObject.length; i++) {
              if (uniqueIdsObject[i].idLanca == objekt.idLanca) {
                uniqueIdsObject[i].oglasi.push(objekt.oglas);
              }
            }
          });

          setLanci(uniqueIdsObject);
          // console.log(uniqueIds);
          // console.log(uniqueIdsObject);
        }
      });
  };

  let lanciComponent = Object.keys(lanci).map((lanac) => {
    return (
      <Card
        title={lanci[lanac].idLanca}
        bordered={false}
        style={{ maxWidth: "720px", margin: "auto", width: "100%" }}
        actions={[
          <div onClick={() => ConfirmSwap(lanci[lanac].idLanca)}>
            <CheckCircleOutlined /> Potvrdi
          </div>,
        ]}
      >
        {lanci[lanac].oglasi.map((oglas) => {
          return (
            <div
              style={{
                borderBottom: "2px solid #f0f0f0",
                marginBottom: "10px",
              }}
            >
              <h3>Student</h3>
              <p>ime: {oglas.student.name}</p>
              <p>email: {oglas.student.email}</p>
              <p>JMBAG: {oglas.student.jmbag}</p>
              <br />
              <h3>Soba</h3>

              <p>Dom: {oglas.dom.imeDoma}</p>
              <p>Paviljon: {oglas.paviljon}</p>
              <p>Kat: {oglas.kat}</p>
              <p>Kategorija sobe: {oglas.kategorijaSobe}</p>
              <br />
            </div>
          );
        })}
      </Card>
    );
  });

  const Confirm = (id) => {
    axios
      .post(
        `${process.env.REACT_APP_API_URL}/api/v1/potvrdiZamjenu`,
        {},
        {
          headers: {
            Authorization: "Bearer " + info.token,
          },
        }
      )
      .then((res) => {
        notification.open({
          message: "Završena zamjena",
          description: "Uspješno ste potvrdili zamjene",
        });
      })
      .catch((res) => {
        console.log("request failed", res);
      });
  };

  useEffect(() => {
    getSwaps();
  }, []);

  return (
    <React.Fragment>
      <Layout className="layout" style={{ minHeight: "100vh" }}>
        <NavBar />
        <Content style={{ padding: "0 50px" }}>
          <div
            className="site-layout-content"
            style={{ maxWidth: "720px", margin: "0 auto" }}
          >
            <br />
          </div>
        </Content>
        {lanciComponent}
        <Footer style={{ textAlign: "center" }}>
          PiccoloGrupa © 2020 FER PI
        </Footer>
      </Layout>
    </React.Fragment>
  );
}

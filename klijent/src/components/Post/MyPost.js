import React, { useContext, useState } from "react";
import { Card, Avatar, Tag } from "antd";
// import { TokenContext } from "../../Context";
// import { LoginContext } from "../../Context";
import EditPost from "./EditPost";
import { InfoContext } from "../../Context";

import { EditOutlined } from "@ant-design/icons";

const { Meta } = Card;

function Post(props) {
  const { info, setInfo } = useContext(InfoContext);
  const [editPostDrawerVisible, setEditPostDrawerVisible] = useState(false);

  const title = "Dom: " + props.title;
  //const description = props.description;
  const idOglas = props.idOglas;
  const kategorija_sobe = props.kategorija_sobe;
  const kat = props.kat;
  const paviljon = props.paviljon;

  // const { token, setToken } = useContext(TokenContext);
  // const { loggedIn, setLoggedIn } = useContext(LoginContext);

  const showDrawer = () => {
    setEditPostDrawerVisible(true);
  };

  const onCloseDrawer = () => {
    setEditPostDrawerVisible(false);
  };

  let aktivanTag = null;
  if (props.aktivan) {
    aktivanTag = <Tag color="green">Aktivan oglas</Tag>;
  } else {
    aktivanTag = <Tag color="red">Neaktivan oglas</Tag>;
  }

  return (
    <div>
      <Card
        style={{ maxWidth: "720px", margin: "auto" }}
        actions={[
          <div onClick={() => showDrawer()}>
            <EditOutlined /> uredi
          </div>,
        ]}
      >
        <Tag color="magenta" style={{ marginBottom: "10px" }}>
          Moja Objava
        </Tag>
        {aktivanTag}
        <br />
        <Meta
          title={title}
          description={
            <React.Fragment>
              <p>Paviljon:&nbsp;{paviljon}</p>
              <p>Kat:&nbsp;{kat}</p>
              <p>Kategorija sobe:&nbsp;{kategorija_sobe}</p>
            </React.Fragment>
          }
        />
      </Card>
      <EditPost
        visible={editPostDrawerVisible}
        closeHandler={() => onCloseDrawer()}
        idOglas={idOglas}
        kat={kat}
        paviljon={paviljon}
        kategorijaSobe={kategorija_sobe}
        dom={props.title}
        idDom={props.idDom}
        token={info.token}
      />

      <br />
    </div>
  );
}

export default Post;

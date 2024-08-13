
console.log("/js/reply.js.....")

// ------------------------------------------------------ //
// 특정 게시글에 대한 댓글 조회 : axios(비동기) 요청 테스트
// ------------------------------------------------------ //
 async function getReply(bno){
  console.log("bno:",bno);

  const result = await axios.get(`/replies/list/${bno}`)
//  console.log("getReply(): ",result)
//  console.log("getReply() data: ",result.data)
//  console.log("getReply() data.list: ",result.data.list)
  return result.data;
}

// ------------------------------------------------------------  //
// 1.게시글에 대한 댓글 List, 인자값이 여러개 전달 받을 경우=> {데이터1,..}
// ------------------------------------------------------------  //
 async function getList({bno, page, size, goLast}){
  const result = await axios.get(
                                  `/replies/list/${bno}`,
                                  { params: {page, size} })
  console.log("axios: getList() data: ",result)

  //onst total = result.data.total; // 댓글 총 개수
  // 댓글 마지막 페이지 계산 = 댓글 총개수/페이지 사이즈 => 자리올림
  //const lastPage = parseInt(Math.ceil(total/size));

//  console.log("total:"+total)
//  console.log("lastPage:"+lastPage)

  if (goLast) {// treu이면 가장 최근글 있는 페이지(막지막) 번호 재요청
    // 댓글 총 개수
    const total = result.data.total;
    // 댓글 마지막 페이지 계산 = 댓글 총개수/페이지 사이즈 => 자리올림
    const lastPage = parseInt(Math.ceil(total/size));


    return getList({bno, page:lastPage, size:size});
  }

  return result.data;
}

// ------------------------------------------------------------  //
// 2.게시글에 대한 댓글 등록
// ------------------------------------------------------------  //
async function addReply(replyObj){
  const response = await axios.post(`/replies/`, replyObj);

   console.log("addReply response:", response.data);
  return response.data
}
// ------------------------------------------------------------  //
// 3.게시글에 대한 댓글 조회
// ------------------------------------------------------------  //
async function getReply(rno){
  const response = await axios.get(`/replies/${rno}`);
  //console.log("addReply response:", response.data);
  return response.data
}

// ------------------------------------------------------------  //
// 4.게시글에 대한 댓글 수정
// ------------------------------------------------------------  //
async function modifyReply(replyObj){
  const response = await axios.put(`/replies/${replyObj.rno}`, replyObj);
  //console.log("addReply response:", response.data);
  return response.data
}
// ------------------------------------------------------------  //
// 5.게시글에 대한 댓글 삭제
// ------------------------------------------------------------  //
async function removeReply(rno){
  const response = await axios.delete(`/replies/${rno}`);
  //console.log("addReply response:", response.data);
  return response.data
}
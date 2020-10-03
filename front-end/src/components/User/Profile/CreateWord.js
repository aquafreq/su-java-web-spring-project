// import React, {useState} from "react";
//
// import {MDBContainer, MDBRow, MDBCol, MDBInput, MDBBtn} from 'mdbreact';
// import {createWord} from '../../../services/UserService';
// import Navigation from "../../Navigation/Navigation";
// import Footer from "../../Footer/Footer";
//
//
// const CreateWord = () => {
//     const [word, setWord] = useState('');
//     const [definition, setDefinition] = useState('');
//
//     const createWord = (word, definition) => {
//
//     }
//
//     return (
//         <MDBContainer>
//             <Navigation/>
//             <MDBRow>
//                 <MDBCol md="6">
//                     <form>
//                         <p className="h5 text-center mb-4">Sign in</p>
//                         <div className="grey-text">
//                             <MDBInput label="Type your word" icon="blind" group type="word" validate error="wrong"
//                                       success="right" value={word} onChange={setWord}
//
//                             />
//                             <MDBInput label="Type your definition" icon="brain" group type="definition" validate
//                                       value={definition} onChange={setDefinition}
//                             />
//                         </div>
//                         <div className="text-center">
//                             <MDBBtn onClick={() => createWord(word, definition)}>Create</MDBBtn>
//                         </div>
//                     </form>
//                 </MDBCol>
//             </MDBRow>
//             <Footer/>
//         </MDBContainer>
//     );
// };
//
// export default CreateWord;
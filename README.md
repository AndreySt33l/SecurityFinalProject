# Project: ETO (Encrypted Transfer Object)

<b>Course name</b>: Information Security and Data Protection<br/>
<b>Student</b>: Stalnoy Andrey Sergeevich<br/>
<b>Lecuter</b>: Altaeva Aygerim Bakhitkaliyevna<br/>

## Description
  The main purpose of the project is to make the "Man in the Middle"(MitM) attack innofficient.<br/>
In order to achive the result, RSA and AES algorithms are implemented into the ETO.class to cipher an incoming or ucoming RAW 
JSON Objecs or JSON Arrays to new JSON Object that consists of two main attributes:<br/>
<ul>
  <li>Content: contetnt represents an Encrypted Json object that was ciphered by AES</li>
  <li>Key: key is AES key that was ciphered by RSA Public key that was received from other person (or server)</li>
</ul>
  

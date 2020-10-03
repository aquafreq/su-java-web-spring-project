import React from 'react'

const UserContext = React.createContext(
    {
        levelOfLanguage: '',
        levelExperience: '',
        id: '',
        username: '',
        userRoles: [],
        birthDate: '',
        nationality: '',
        hobbies: []
    }
)

export default UserContext;
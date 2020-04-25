import React, {Component} from 'react'

const TableHead = (props) => {
    const {name, job, age } = props.headData

    return (
        <thead>
        <tr>
            <th>{name}</th>
            <th>{age}</th>
            <th>{job}</th>
        </tr>
        </thead>
    )
}

const TableBody = props => {
    const rows = props.characters.map((row, index) => {
        return (
            <tr key={index}>
                <td>{row.name}</td>
                <td>{row.age}</td>
                <td>{row.job}</td>
                <td>
                    <button onClick={() => props.removeCharacter(index)}>Delete</button>
                </td>
            </tr>
        )
    })

    return <tbody>{rows}</tbody>
}

const Table = props => {
    const {characterData, removeCharacter, headData} = props;

    return (
        <table>
            <TableHead headData={ headData }/>
            <TableBody characters={characterData} removeCharacter={removeCharacter} />
        </table>
    )
}

export default Table
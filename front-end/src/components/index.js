import React from 'react'

export default function ({labelName, value, onChange, ...rest}) {
    return (
        <div>
            <label>
                {labelName}
                <input  type="text" value={value} onChange={onChange} {...rest}/>
            </label>
        </div>
    )
}

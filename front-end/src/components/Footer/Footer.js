import React from 'react'
import styles from './Footer.module.css'

const Footer = () => (
    <div className={styles.footer}>
        <div className={styles.picWrapper}>
            <footer>
                <h6>This is Hello-English</h6>
                Contact me on <span>requiemforadream@abv.bg</span>
            </footer>
        </div>
    </div>
);

export default Footer;
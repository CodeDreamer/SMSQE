* Text for error window                 v0.00  June 1988  J.R.Oakley  QJUMP
*
        section language
*
        include 'dev8_mac_text'
        include 'dev8_keys_k'
*
        mkxstr  esc,k.cancel,{ESC}
*
erstr1  setstr  {Ce programme s'est arr‘tƒ avec l'erreur\}
*
        mktext  erms                    \
                {[erstr1]}
*
        xdef    msx.erms
        xdef    msy.erms
msx.erms equ    [.len(erstr1)]*6
msy.erms equ    10
*
        end

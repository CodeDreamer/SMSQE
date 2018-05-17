; Text for error window                 v0.00  June 1988  J.R.Oakley  QJUMP

        section language

        include 'dev8_mac_text'
        include 'dev8_keys_k'

erstr1  setstr  {This application failed with the error\}

        mktext  erms                    \
                {[erstr1]}

        xdef    msx.erms
        xdef    msy.erms
msx.erms equ    [.len(erstr1)]*6
msy.erms equ    10

        end

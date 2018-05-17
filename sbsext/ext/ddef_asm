* Find the directory defaults    1985  Tony Tebby  QJUMP 
*
        section exten
*
*       DLIST channel number    list the defaults
*
*       DATAD$                  return data default
*       PROGD$                  return program default
*       DESTD$                  return destination default
*
        xdef    dlist
        xdef    datad$
        xdef    progd$
        xdef    destd$
*
        xref    ut_chan1
        xref    ut_wrta1
        xref    ut_wrtnl
        xref    ut_par0
        xref    ut_fdef
        xref    ut_rtstr
*
        include dev8_sbsext_ext_keys
dlist
        bsr.l   ut_chan1                find channel ID
        bsr.s   datad$                  data default
        bne.s   dd_rts
        bsr.s   ddwrite                 write it
        bsr.s   progd$                  program default
        bsr.s   ddwrite                 write it
        bsr.s   destd$                  destination default
ddwrite
        bsr.l   ut_wrta1                write what is at A1
        bra.l   ut_wrtnl                and newline
dd_rts
        rts
*
destd$
        moveq   #sv_destd-sv_fdefo,d2
        bra.s   anyd_
progd$
        moveq   #sv_progd-sv_fdefo,d2
        bra.s   anyd_
datad$
        moveq   #sv_datad-sv_fdefo,d2
anyd_
        bsr.l   ut_par0                 no parameters
        bsr.l   ut_fdef                 get file default pointer
        bra.l   ut_rtstr                and return string
        end

* Set window definition list   V1.00     1986  Jonathan Oakley  QJUMP
*
        section driver
*
        include 'dev8_keys_con'
*
        xdef    pt_swdef
*
pt_swdef
        move.l  a1,sd_wwdef(a0)         set definition pointer
        clr.b   pt_schfg(a3)            and wait for scheduler
        moveq   #0,d0
        rts
        end

* Draw sub-window indices    V1.02     1986  Tony Tebby   QJUMP
*
        section wman
*
        xdef    wm_index
*
        xref    wm_swdef
        xref    wm_drpns
        xref    wm_drbar
*
*       d0  r   error return
*       a0 c  p channel ID of window
*       a3 c  p pointer to sub-window definition
*       a4 c  p pointer to working definition
*
*               all other registers preserved
*
reglist  reg    d1-d7/a1/a2/a4
*
wm_index
        movem.l reglist,-(sp)           save registers
        bsr.l   wm_drbar                draw bars
        bsr.l   wm_swdef                set sub window
        bne.s   wix_exit
        bsr.l   wm_drpns                draw pan and scroll bars
*
wix_exit
        tst.l   d0
        movem.l (sp)+,reglist           restore registers
        rts
        end

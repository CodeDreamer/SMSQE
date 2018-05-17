* Draw Loose menu items   V1.01     1986  Tony Tebby   QJUMP
*
        section wman
*
        xdef    wm_ldraw
*
        xref    wm_drlit
*
        include dev8_keys_qdos_io
        include dev8_keys_wwork
*
*       d0  r   error return
*       d3 c  p 0 byte draw all, -byte selective
*       a0 c  p channel ID of window
*       a1   sp pointer to status area
*       a4 c  p pointer to working definition
*
*               all other registers preserved
*
reglist  reg    d1-d3/a1/a2/a3
stk_act  equ    8
*
wm_ldraw
        movem.l reglist,-(sp)           save registers
        move.l  ww_plitm(a4),a3         pointer to loose menu items
        move.l  a3,d0                   any?
        beq.s   wld_exit                ... no
        lea     ww_xsize(a4),a1         pointer to window definition
        moveq   #iow.defw,d0            ... redefine it
        moveq   #0,d2                   ... no border
        moveq   #-1,d3
        trap    #3
*
        move.l  stk_act(sp),d3          set force redraw flag
        move.l  ww_wstat(a4),a1         pointer to window status area
wld_item
        bsr.l   wm_drlit                draw loose item
        bne.s   wld_exit                ... oops
        add.l   #wwl.elen,a3            next loose item
        tst.w   (a3)                    end?
        bge.s   wld_item                ... no
        moveq   #0,d0                   ... yes
wld_exit
        movem.l (sp)+,reglist           restore registers
        rts
        end

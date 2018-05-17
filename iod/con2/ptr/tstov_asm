*       Test a window to see if its outline overlaps a given area.
*
*       Registers:
*               Entry                   Exit
*       D0                              0 for overlap, -1 for no overlap
*       D1      xmax | ymax
*       D2      xmin | ymin
*       A0      ^ cdb of window
*
        section driver
*
        include 'dev8_keys_con'
*
        xdef    pt_tstov
*
pt_tstov
        bsr.s   chk_lim                 ; check the x limits
        ble.s   not_ovs                 ; no overlap
        addq.l  #2,a0                   ; point to window's y co-ordinates
        bsr.s   chk_lim                 ; check those
        subq.l  #2,a0                   ; get cdb back again
        ble.s   not_over
        moveq   #0,d0                   ; flag overlap
        bra.s   exit                    ; and exit
not_ovs
        swap    d1
        swap    d2
not_over
        moveq   #-1,d0                  ; flag no overlap
exit
        rts
*
chk_lim
        swap    d1
        swap    d2
        move.w  sd_xouto(a0),d0
        cmp.w   d0,d1                   ; compare window xmin with xmax
        ble.s   ex_chk                  ; it's greater, so no overlap
        add.w   sd_xouts(a0),d0         ; get window xmax
        cmp.w   d2,d0                   ; and compare with xmin
ex_chk
        rts
*
        end

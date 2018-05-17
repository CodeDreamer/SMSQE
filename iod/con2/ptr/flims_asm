* Read window limits                    V1.00   Oct 1987  J.R.Oakley  QJUMP
*
        section driver
*
        include 'dev8_keys_err'
        include 'dev8_keys_con'
*
        xdef    pt_flims
*
pt_flims
        tst.b   d2                      ; is it standard?
        bne.s   ptf_exbp                ; ... no, error for now
        move.l  pt_ssize(a3),d2         ; get x and y max
        move.l  #0,d3                   ; and min
        tst.b   sd_prwin(a0)            ; is it a primary?
        bmi.s   ptf_set                 ; ... yes
        move.l  sd_pprwn(a0),a2         ; get primary window
        movem.l sd_xhits+sd.extnl(a2),d2/d3 ; and primary hit area
ptf_set
        movem.l d2/d3,(a1)              ; return permissible limits
        moveq   #0,d0
ptf_exit
        rts
ptf_exbp
        moveq   #err.ipar,d0
        bra.s   ptf_exit
*
        end

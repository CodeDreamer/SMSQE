; QL timing constants

        section init

        xdef    nd_ql

        xref    nd_initb

nd_ql
        lea     ndt_ql,a1
        jmp     nd_initb

ndt_ql
ndt_wgap dc.w      26-1   ; wait for gap constant                200us
ndt_lsct                  ; look for scout                     20000us
ndt_bace dc.w    2600-1   ; look for broadcast acknowledge end 20000us
ndt_csct dc.w       4-1   ; check scout                           30us
ndt_esct dc.w     198-1   ; end of scout                         475us
ndt_wsct dc.w     390-1   ; wait to send scout                  3000us
ndt_tsct dc.w       4     ; timer for scout active / inactive      4
ndt_bsct                  ; broadcast scout detect               500us
ndt_back dc.w      65-1   ; broadcast acknowledge detect         500us
ndt_xsct                  ; extension to scout                  5000us
ndt_xack dc.w    2083-1   ; (extension to) acknowledge          5000us
ndt_bnak dc.w      83-1   ; broadcast NACK delay                 200us
ndt_stmo dc.w    5357     ; serial port timout                 20000us
ndt_paus dc.w      50-1   ; pause before send                    120us
ndt_send dc.w       2     ; send loop timer                        2
ndt_rtmo dc.w     625-1   ; receive timout                      2500us
ndt_rbto dc.w      20-1   ; receive bit timout                    80us
ndt_rdly dc.w       6     ; receive delay start bit to bit 0       6
ndt_rbit dc.w       5     ; receive bit loop timer                 5
        end
